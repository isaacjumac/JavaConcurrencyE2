package javaconcurrencye3.executorframework.costposting.service;

import com.google.common.collect.Lists;
import javaconcurrencye3.executorframework.costposting.dao.CostPostingDao;
import javaconcurrencye3.executorframework.costposting.entity.CostPostingParam;
import javaconcurrencye3.executorframework.costposting.entity.CostPostingResult;
import javaconcurrencye3.executorframework.costposting.exception.CostPostingException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CostPostingService {
    private final DataComplementService complementService;
    private final ValidationService validationService;
    private final CostPostingDao dao;

    private static final int NUM_THREADS = 10;
    private final ExecutorService fixedThreadExecutor = Executors.newFixedThreadPool(NUM_THREADS); // create thread pool with fix size of threads
    // we need to keep a volatile variable in order to know if something is wrong with a particular sub-thread
    // but this is BAD, as it forces us to maintain a state of our service
    volatile boolean somethingWrong = false;


    public void registerCost(CostPostingParam param) {
        // Split the dtls to 10 per group
        List<CostPostingParam> params = Lists.partition(param.getDetails(), 10).stream()
                .map(dtls -> {
                    CostPostingParam p = param.makeClone();
                    p.setDetails(dtls);
                    return p;
                })
                .collect(Collectors.toList());

        // Process by the 10-dtl group
        // Each group a thread
        for (CostPostingParam p : params) {
            if (somethingWrong) break; // stop processing more data when there's something wrong with existing data
            new Thread(() -> {
                try {
                    processingCostActual(p);
                } catch (Exception e) {
                    somethingWrong = true;
                    e.printStackTrace(); // you literally cannot throw an exception to stop the execution of the for loop here
                }
            }
            ).start();
        }
    }

    public CostPostingResult registerCostWithFixedNumThreads(CostPostingParam param) {
        somethingWrong = false; // initialize without issue so that cost posting can proceed
        // Split the dtls to 10 per group
        List<CostPostingParam> params = Lists.partition(param.getDetails(), 10).stream()
                .map(dtls -> {
                    CostPostingParam p = param.makeClone();
                    p.setDetails(dtls);
                    return p;
                })
                .collect(Collectors.toList());

        // process by the 10-dtl group
        // process using a thread pool with 10 threads
        List<Future<CostPostingResult>> rstFutures = new ArrayList<>();
        for (CostPostingParam p : params) {
            try {
                Future<CostPostingResult> rstFuture = fixedThreadExecutor.submit(() -> processingCostActual(p));
                rstFutures.add(rstFuture);
                CostPostingResult rst = rstFuture.get(); // blocking
                if (!rst.isSuccessful()) {
                    return CostPostingResult.builder().paramId(param.getId()).successful(false).build();
                }
            } catch (RejectedExecutionException | ExecutionException | InterruptedException e) {
                fixedThreadExecutor.shutdown(); // try a smooth shutdown: not accepting new tasks, and wait till current tasks to complete execution
                return CostPostingResult.builder().paramId(param.getId()).successful(false).build();
            }
        }

        return CostPostingResult.builder().paramId(param.getId()).successful(true).build();
    }

    private CostPostingResult processingCostActual(CostPostingParam param) throws CostPostingException {
        System.out.println("Number of active threads: " + Thread.activeCount());
        System.out.println("Current thread: " + Thread.currentThread().getName());
        try {
            complementService.complement(param);
            if (validationService.validate(param)) {
                dao.insert(param);
            }
            return CostPostingResult.builder().paramId(param.getId()).successful(true).build();
        } catch (Exception e) {
            throw new CostPostingException(e);
        }
    }
}


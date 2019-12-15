package javaconcurrencye3.executorframework.costposting.service;

import javaconcurrencye3.executorframework.costposting.entity.CostPostingParam;

public class ValidationService {
    public boolean validate(CostPostingParam param) {
        return param.getDetails().stream()
                .map(dtl -> dtl.getAccountId() != null && dtl.getProjectId() != null)
                .reduce(true, (a, b) -> a && b);
    }
}

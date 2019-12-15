package javaconcurrencye3.executorframework.costposting.service;

import javaconcurrencye3.executorframework.costposting.entity.CostPostingDtl;
import javaconcurrencye3.executorframework.costposting.entity.CostPostingParam;

public class DataComplementService implements IDataComplementService {
    public void complement(CostPostingParam param) {
        for (CostPostingDtl dtl : param.getDetails()) {
            if (dtl.getProjectId() != null) {
                dtl.setProjectName("Project-" + dtl.getProjectId().toString().substring(dtl.getProjectId().toString().length() - 8));
            }
            if (dtl.getAccountId() != null) {
                dtl.setAccountName("Account-" + dtl.getAccountId().toString().substring(dtl.getAccountId().toString().length() - 8));
            }
        }
    }

    public void complement(CostPostingDtl dtl) {
        if (dtl.getProjectId() != null) {
            dtl.setProjectName("Project-" + dtl.getProjectId().toString().substring(dtl.getProjectId().toString().length() - 8));
        }
        if (dtl.getAccountId() != null) {
            dtl.setAccountName("Account-" + dtl.getAccountId().toString().substring(dtl.getAccountId().toString().length() - 8));
        }
    }
}

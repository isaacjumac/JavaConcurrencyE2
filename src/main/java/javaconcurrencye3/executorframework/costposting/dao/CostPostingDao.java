package javaconcurrencye3.executorframework.costposting.dao;

import javaconcurrencye3.executorframework.costposting.entity.CostPostingParam;

public class CostPostingDao {
    public void insert(CostPostingParam param){
        System.out.println("Inserted cost entry with ID: "+ param.getId());
    }
}

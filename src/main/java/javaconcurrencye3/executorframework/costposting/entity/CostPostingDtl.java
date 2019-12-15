package javaconcurrencye3.executorframework.costposting.entity;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CostPostingDtl {
    private int rowNo;
    private UUID projectId;
    private String projectName;
    private UUID accountId;
    private String accountName;
}

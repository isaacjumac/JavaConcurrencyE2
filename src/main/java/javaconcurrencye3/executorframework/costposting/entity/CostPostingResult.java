package javaconcurrencye3.executorframework.costposting.entity;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CostPostingResult {
    UUID paramId;
    boolean successful;
}

package javaconcurrencye3.executorframework.costposting.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CostPostingParam {
    private UUID id;
    private BigDecimal amount;
    private List<CostPostingDtl> details;

    public CostPostingParam makeClone(){
        return CostPostingParam.builder()
                .id(id)
                .amount(amount)
                .details(details)
                .build();
    }
}

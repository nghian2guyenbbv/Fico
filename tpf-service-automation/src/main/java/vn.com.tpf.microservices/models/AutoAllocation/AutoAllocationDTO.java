package vn.com.tpf.microservices.models.AutoAllocation;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AutoAllocationDTO {
    private String project;
    private String reference_id;
    private List<AutoReassignUserDTO> reassign = null;
    private List<AutoAssignAllocationDTO> autoAssign = null;
}

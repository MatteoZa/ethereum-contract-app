package ethereum.contract.app.backend.dto;

import ethereum.contract.app.backend.enums.ObjectType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ContractObjectDto implements Serializable {
    private Boolean constant;
    private List<FieldDto> inputs;
    private String name;
    private List<FieldDto> outputs;
    private Boolean payable;
    private String stateMutability;
    private ObjectType type;
    private Boolean anonymous;
}

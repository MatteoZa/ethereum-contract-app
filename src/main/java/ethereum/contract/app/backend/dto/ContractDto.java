package ethereum.contract.app.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class ContractDto implements Serializable {
    private List<ContractObjectDto> contractObjects;
}

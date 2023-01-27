package ethereum.contract.app.backend.dto;

import ethereum.contract.app.backend.enums.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldDto implements Serializable {
    private String name;
    private FieldType type;
}

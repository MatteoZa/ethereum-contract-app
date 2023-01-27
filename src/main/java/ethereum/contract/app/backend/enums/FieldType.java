package ethereum.contract.app.backend.enums;

import ethereum.contract.app.backend.exception.EnumValueNotFoundException;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum FieldType {
    STRING("string"),
    ADDRESS("address"),
    UINT8("uint8"),
    UINT256("uint256"),
    BOOL("bool");

    private String text;

    public static FieldType getValue(String fieldType) {
        return Arrays.stream(values())
                .filter(type -> fieldType.equals(type.text))
                .findFirst()
                .orElseThrow(() -> new EnumValueNotFoundException("FieldType", fieldType));
    }
}

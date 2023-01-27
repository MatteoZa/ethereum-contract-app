package ethereum.contract.app.backend.enums;

import ethereum.contract.app.backend.exception.EnumValueNotFoundException;
import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum ObjectType {
    FUNCTION("function"),
    EVENT("event"),
    FALLBACK("fallback");

    private String text;

    public static ObjectType getValue(String objectType) {
        return Arrays.stream(values())
                .filter(type -> objectType.equals(type.text))
                .findFirst()
                .orElseThrow(() -> new EnumValueNotFoundException("ObjectType", objectType));
    }
}

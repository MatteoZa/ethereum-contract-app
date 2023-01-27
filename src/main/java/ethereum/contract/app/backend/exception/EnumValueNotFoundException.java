package ethereum.contract.app.backend.exception;

public class EnumValueNotFoundException extends RuntimeException {

    public EnumValueNotFoundException(String enumClass, String objectType) {
        super("Enum value provided not found for enum class " + enumClass + ": '" + objectType + "'");
    }

}

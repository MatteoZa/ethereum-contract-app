package ethereum.contract.app.backend.exception;

public class FunctionNotFoundException extends RuntimeException {

    public FunctionNotFoundException(String functionName) {
        super("Function provided not found: " + functionName);
    }

}

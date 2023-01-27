package ethereum.contract.app.backend.exception;

public class MissingContractException extends RuntimeException {

    public MissingContractException() {
        super("Please import ABI first");
    }

}

package ethereum.contract.app.backend.exception;

public class BadAbiFormatException extends RuntimeException {

    public BadAbiFormatException() {
        super("The ABI format provided is not supported");
    }

}

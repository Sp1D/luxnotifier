package sp1d.luxnotifier;

public class LuxnotifierRuntimeException extends RuntimeException {
    public LuxnotifierRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LuxnotifierRuntimeException(Throwable cause) {
        super(cause);
    }

    public LuxnotifierRuntimeException(String message) {
        super(message);
    }
}

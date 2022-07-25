package chen.eric.cats;

public class CatsRuntimeException extends RuntimeException {
	public CatsRuntimeException() {
	}

	public CatsRuntimeException(String message) {
		super(message);
	}

	public CatsRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public CatsRuntimeException(Throwable cause) {
		super(cause);
	}
}

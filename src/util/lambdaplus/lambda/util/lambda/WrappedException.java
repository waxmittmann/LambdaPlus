package util.lambdaplus.lambda.util.lambda;

public class WrappedException extends RuntimeException {
    private final Exception e;

    public WrappedException(Exception e) {
        super(String.format("Wrapped Exception: %s", e.getMessage()));
        this.e = e;
    }

    public Exception getWrappedException() {
        return e;
    }
}

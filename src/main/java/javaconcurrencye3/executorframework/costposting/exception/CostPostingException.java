package javaconcurrencye3.executorframework.costposting.exception;

public final class CostPostingException extends Exception {
    public CostPostingException(String message) {
        super(message);
    }

    public CostPostingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CostPostingException(Throwable cause) {
        super(cause);
    }
}

public class ExceptionDataFetcher extends Exception {

    public ExceptionDataFetcher(Throwable throwable) {
        super(throwable);
    }

    public ExceptionDataFetcher(String message, Throwable e) {
        super(message, e);
    }
}

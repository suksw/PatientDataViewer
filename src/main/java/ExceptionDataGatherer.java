public class ExceptionDataGatherer extends Exception {

    public ExceptionDataGatherer(Throwable throwable) {
        super(throwable);
    }

    public ExceptionDataGatherer(String message, Throwable e) {
        super(message, e);
    }
}

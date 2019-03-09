package nl.klokhet.app.flow.base.error;

public final class NotImplementedInterfaceException extends ClassCastException {

    private static final String MESSAGE = "You need to implement %s";

    public <T> NotImplementedInterfaceException(Class<T> clazz) {
        super(String.format(MESSAGE, clazz));
    }
}
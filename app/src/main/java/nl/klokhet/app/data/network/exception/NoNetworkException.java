package nl.klokhet.app.data.network.exception;


import nl.klokhet.app.App;
import nl.klokhet.app.R;

public class NoNetworkException extends Exception {

    private static final String ERROR_MESSAGE = App.getInstance().getString(R.string.no_internet_connection);

    public NoNetworkException() {

    }

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }
}

package nl.klokhet.app.data.network.exception;


import nl.klokhet.app.App;
import nl.klokhet.app.R;

public class ServerException extends ApiException {

    private static final String ERROR_MESSAGE = App.getInstance().getString(R.string.server_error);

    public ServerException() {
        //
    }

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }


    @Override
    public Integer getStatusCode() {
        return 500;
    }
}

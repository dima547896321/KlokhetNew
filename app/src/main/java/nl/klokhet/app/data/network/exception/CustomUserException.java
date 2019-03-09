package nl.klokhet.app.data.network.exception;


import nl.klokhet.app.App;
import nl.klokhet.app.R;
import retrofit2.HttpException;

public class CustomUserException extends ApiException {

    private static final String ERROR_MESSAGE = App.getInstance().getString(R.string.server_error);
    private HttpException mHttpException;

    public CustomUserException(HttpException ex) {
        mHttpException = ex;
    }

    @Override
    public String getMessage() {
        return ERROR_MESSAGE;
    }


    @Override
    public Integer getStatusCode() {
        return 447;
    }

    public HttpException getHttpException() {
        return mHttpException;
    }
}

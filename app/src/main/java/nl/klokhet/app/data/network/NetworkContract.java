package nl.klokhet.app.data.network;

public class NetworkContract {
    private NetworkContract() {
        //no instance
    }

    static final class HttpStatus {

        static final int UNAUTHORIZED = 401;
        static final int FORBIDDEN = 403;
        static final int USER_NOT_FOUND = 404;

        private HttpStatus() {
            //no instance
        }
    }
}

package nl.klokhet.app.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import nl.klokhet.app.data.network.exception.ApiException;
import nl.klokhet.app.data.network.exception.CustomUserException;
import nl.klokhet.app.data.network.exception.NoNetworkException;
import nl.klokhet.app.data.network.exception.ServerException;
import nl.klokhet.app.data.network.exception.bean.ServerError;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by Dima on 24.03.2018.
 */

public class NetworkErrorUtil {
    private static final int SERVER_ERROR_CODE_START = 500;
    private static final int SERVER_ERROR_CODE_END = 500;
    private static final int USER_ERROR_CODE = 447;

    private static final String TAG = "NetworkErrorUtils";

    private static final ObjectMapper sObjectMapper;

    static {
        sObjectMapper = new ObjectMapper();
        sObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public static NetworkErrorUtil instance() {
        return Loader.S_NETWORK_ERROR_UTIL;
    }

    public static boolean isServerConnectionProblem(Throwable throwable) {
        return throwable instanceof SocketException || throwable instanceof SocketTimeoutException;
    }

    public static boolean isConnectionProblem(Throwable throwable) {
        return throwable instanceof UnknownHostException || throwable instanceof ConnectException;
    }

    public <T> Function<Throwable, Observable<T>> rxParseError() {
        return throwable -> Observable.error(parseError(throwable));
    }

    public <T> Function<Throwable, Single<T>> rxParseErrorSingle() {
        return throwable -> Single.error(parseError(throwable));
    }

    public Function<Throwable, Completable> rxParseErrorCompletable() {
        return throwable -> Completable.error(parseError(throwable));
    }

    public <T> Observable<T> parseErrorObservable(HttpException e) {
        return Observable.error(parseError(e));
    }

    private Throwable parseError(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            // return this exception in case of error with 500 code
            if (httpException.code() >= SERVER_ERROR_CODE_START && httpException.code() <= SERVER_ERROR_CODE_END) {
                return new ServerException().initCause(httpException);
            }
            // return this exception in case of error with 447 code
            if (httpException.code() == USER_ERROR_CODE) {
                //LOG.e(httpException);
                return new CustomUserException(httpException);
            }
            return parseErrorResponseBody(((HttpException) throwable).response());
        } else if (isConnectionProblem(throwable)) {
            return new NoNetworkException();
        } else if (isServerConnectionProblem(throwable)) {
            return new ServerException();
        } else {
            return throwable;
        }
    }

    private Exception parseErrorResponseBody(Response<?> response) {
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStreamReader = new InputStreamReader(response.errorBody().byteStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String newLine;
            while ((newLine = bufferedReader.readLine()) != null) {
                sb.append(newLine);
            }

            // Try to parse ServerError.class
            ServerError serverError;
            try {
                serverError = sObjectMapper.readValue(sb.toString(), ServerError.class);
            } catch (IOException e) {
                return e;
            }


            return new ApiException(response.code(), serverError.getError());


        } catch (IOException e) {
            return e;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static final class Loader {
        private static final NetworkErrorUtil S_NETWORK_ERROR_UTIL =
                new NetworkErrorUtil();

        private Loader() throws IllegalAccessException {
            throw new IllegalAccessException("Loader class");
        }
    }

}

package nl.klokhet.app.data.network.providers;

public class ProviderModule {
    public static UserProviderImpl getUserProvider() {
        return UserProvider.S_USER_PROVIDER;
    }

    private static final class UserProvider {
        private static final UserProviderImpl S_USER_PROVIDER = new UserProviderImpl();
    }
}

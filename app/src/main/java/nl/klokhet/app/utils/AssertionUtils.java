package nl.klokhet.app.utils;


public class AssertionUtils {
    private AssertionUtils() {
    }

    public static <T> void assertNotNull(T object,String parameterName) throws AssertionError {
        if (object == null)
            throw new AssertionError(parameterName + " can't be null.");
    }

    public static <T> void assertInstanceOf( T object, Class<?> clazz,  String parameterName) throws AssertionError {
        check(!clazz.isInstance(object), parameterName + " is not instance of " + clazz.getName() + ".");
    }

    public static <T> void assertNotEquals(T object,  T anotherObject,  String parameterName) throws AssertionError {
        check(object == anotherObject || object.equals(anotherObject), parameterName + " can't be equal to " + String.valueOf(anotherObject) + ".");
    }

    public static void check(boolean b,  String message) {
        if (b)
            throw new AssertionError(message);
    }
}


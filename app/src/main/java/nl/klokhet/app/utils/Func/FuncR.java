package nl.klokhet.app.utils.Func;

/**
 * Functional interface
 *
 * @param <T> Type for return
 */
@FunctionalInterface
public interface FuncR<T> {
    T call();
}

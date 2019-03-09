package nl.klokhet.app.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Predicate;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T> Collection<T> removeNullElements(@NonNull Collection<T> collection) {
        Iterator<T> tIterator = collection.iterator();
        while (tIterator.hasNext()) {
            T item = tIterator.next();
            if (item == null) {
                tIterator.remove();
            }
        }
        return collection;
    }

    public static <T> List<T> convertIterableToList(@NonNull Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        Iterator<T> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    public static <T> Observable<List<T>> convertIterableToListRx(@NonNull Iterable<T> iterable) {
        return Observable.just(convertIterableToList(iterable));
    }

    public static <T> ObservableTransformer<Iterable<T>, List<T>> convertIterableToListTransformer() {
        return iterableObservable -> iterableObservable.map(CollectionUtils::convertIterableToList);
    }

    public static boolean isNullOrEmpty(@Nullable Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean containsAnySafe(@Nullable Collection<T> collection, T... items) {
        if (isNullOrEmpty(collection)) {
            return false;
        }
        for (T item : items) {
            if (collection.contains(item)) {
                return true;
            }
        }
        return false;
    }

    public static <T> Maybe<Integer> findPositionByPredicate(List<T> list, Predicate<T> predicate) {
        return Maybe.create(emitter -> {
            for (int i = 0; i < list.size(); i++) {
                if (emitter.isDisposed()) {
                    emitter.onComplete();
                    return;
                }
                T item = list.get(i);
                if (predicate.test(item)) {
                    emitter.onSuccess(i);
                    emitter.onComplete();
                    return;
                }
            }
            emitter.onComplete();
        });
    }

    public static <T> void addIfNotNull(List<T> list, T item) {
        if (item != null) {
            list.add(item);
        }
    }

    public static <T> void addIfNotNull(List<T> list, T... items) {
        for (T item : items) {
            if (item != null) {
                list.add(item);
            }
        }
    }

    public static <T> List<T> firstNElements(@NonNull List<T> list, int count) {
        if (count > list.size()) {
            return list;
        }
        return list.subList(0, count);
    }

}

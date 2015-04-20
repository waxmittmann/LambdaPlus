package util.lambdaplus.lambda.either;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface EitherFunction<T, R, L> {

    Either<L, R> apply(T t);

    default <V> EitherFunction<T, V, L> andThen(EitherFunction<R, V, L> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            Either<L, R> result = apply(t);
            Function<R, Either<L, V>> rvFunction = (r) -> after.apply(r);
            return result.flatMapRight(rvFunction);
        };
    }

    static <L, R> Either<L, R> doInTurn(Object seed, EitherFunction<?, ?, L>... funcs) {
        Objects.requireNonNull(seed);
        Objects.requireNonNull(funcs);

        Either<L, Object> cur = new Right<>(seed);

        for(EitherFunction func : funcs) {
            cur = func.apply(cur.getRightValueOrException());
            if (cur.isLeft())
                return (Either<L, R>) cur;
        }
        return (Either<L, R>) cur;
    }

    static <L, R, S> Function<S, Either<L, R>> createDoInTurn(EitherFunction<?, ?, L>... funcs) {
        Function<S, Either<L, R>> compositeFunc = (S s) -> doInTurn(s, funcs);
        return compositeFunc;
    }

    static <T, R, L> EitherFunction<T, R, L> create(Function<T, Either<L, R>> func) {
        return t -> func.apply(t);
    }
}
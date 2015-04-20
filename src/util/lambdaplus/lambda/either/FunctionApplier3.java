package util.lambdaplus.lambda.either;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface FunctionApplier3<T, R, L> {

    Either<L, R> apply(T t);

    //    default <V> FunctionApplier3<T, V, L> andThen(FunctionApplier3<? super R, ? extends V, L> after) {
    default <V> FunctionApplier3<T, V, L> andThen(FunctionApplier3<R, V, L> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            Either<L, R> result = apply(t);
            Function<R, Either<L, V>> rvFunction = (r) -> after.apply(r);
            return result.flatMapRight(rvFunction);
        };
    }

//    static <L, R> Either<L, R> doInTurn(Object seed, FunctionApplier3<?, ?, L> [] funcs) {
//        Either<L, Object> cur = new Right<>(seed);
//
//        for(FunctionApplier3 func : funcs) {
//            cur = func.apply(cur.getRightValueOrException());
//            if (cur.isLeft())
//                return (Either<L, R>) cur;
//        }
//        return (Either<L, R>) cur;
//    }

    static <L, R> Either<L, R> doInTurn(Object seed, FunctionApplier3<?, ?, L> ... funcs) {
        Either<L, Object> cur = new Right<>(seed);

        for(FunctionApplier3 func : funcs) {
            cur = func.apply(cur.getRightValueOrException());
            if (cur.isLeft())
                return (Either<L, R>) cur;
        }
        return (Either<L, R>) cur;
    }

    static <L, R, S> Function<S, Either<L, R>> createDoInTurn(FunctionApplier3<?, ?, L>... funcs) {
        Function<S, Either<L, R>> compositeFunc = (S s) -> {
            Either<L, Object> cur = new Right<>(s);

            for(FunctionApplier3 func : funcs) {
                cur = func.apply(cur.getRightValueOrException());
                if (cur.isLeft()) {
                    return new Left<L, R>(cur.getLeft().get());
//                    return (Either<L, R>) cur;
                }
            }
            return new Right<L, R>((R) cur.getRight().get());
//            return (Either<L, R>) cur;
        };

        return compositeFunc;

//        return s -> doInTurn((Object) s, funcs);
    }
}
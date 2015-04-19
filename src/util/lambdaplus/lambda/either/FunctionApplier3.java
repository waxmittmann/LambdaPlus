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
}
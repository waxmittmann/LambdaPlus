package util.lambdaplus.lambda.util.lambda;

import util.lambdaplus.lambda.either.Either;
import util.lambdaplus.lambda.either.EitherFunction;
import util.lambdaplus.lambda.either.Left;

import java.util.Objects;

//ThrowingFunction can't extend Function (because it throws Exception) but both EitherFunction and LiftingFunction can
@FunctionalInterface
public interface ThrowingFunction<T, R> {
    R apply(T t) throws Exception;

    default <V> EitherFunction<V, Exception, R> compose(ThrowingFunction<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> {
            Either<Exception, ? extends T> result = LambdaUtils.liftThrowable(before).apply(v);
            if (result.isLeft()) {
                return new Left<Exception, R>(result.getLeft().get());
            } else {
                return LambdaUtils.liftThrowable(this).apply(result.getRightValueOrException());
            }
        };
    }

    default <V> EitherFunction<T, Exception, V> andThen(ThrowingFunction<? super R, V> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            Either<Exception, R> result = LambdaUtils.liftThrowable(this).apply(t);
            if (result.isLeft()) {
                return new Left<>(result.getLeft().get());
            } else {
                R rightValue = result.getRightValueOrException();
                Either<Exception, V> finalResult = LambdaUtils.liftThrowable(after).apply(rightValue);
                return finalResult;
            }
        };
    }

    static <V> ThrowingFunction<V, V> identity() {
        return v -> v;
    }
}

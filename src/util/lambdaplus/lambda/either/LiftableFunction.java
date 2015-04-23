package util.lambdaplus.lambda.either;

import util.lambdaplus.lambda.util.LambdaUtils;
import util.lambdaplus.lambda.util.ThrowableFunction;

import java.util.Objects;

//Todo: Collapse into ThrowableFunction?
@FunctionalInterface
public interface LiftableFunction<T, R> {
    default <V> EitherFunction<V, Exception, R> compose(LiftableFunction<? super V, ? extends T> before) {
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

    default <V> EitherFunction<T, Exception, V> andThen(LiftableFunction<? super R, V> after) {
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

    static <T> LiftableFunction<T, T> identity() {
        return (T t) -> t;
    }
}

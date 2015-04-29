/*
 * Copyright (C) 2015 Covata Limited or its affiliates
 *
 * Information contained within this file cannot be copied,
 * distributed and/or practised without the written consent of
 * Covata Limited or its affiliates.
 */

package util.lambdaplus.lambda.util.lambda;

import util.lambdaplus.lambda.either.Either;
import util.lambdaplus.lambda.either.Left;
import util.lambdaplus.lambda.either.Right;

import java.util.function.Function;
import java.util.function.Supplier;

public class LambdaUtils {

    public static <T> T wrapError(ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new WrappedException(e);
        }
    }

    public static <T> Either<Exception, T> runCatch(ThrowingSupplier<T> supplier) {
        try {
            return new Right(supplier.get());
        } catch (Exception e) {
            return new Left(e);
        }
    }

    public static <T> Either<Exception, T> runCatchWrapped(Supplier<T> supplier) {
        try {
            return new Right(supplier.get());
        } catch (WrappedException e) {
            return new Left(e.getWrappedException());
        }
    }

    public static <S, T> Function<S, T> wrapThrowable(ThrowingFunction<S, T> function) {
        return s -> {
            try {
                return function.apply(s);
            } catch (Exception e) {
                throw new WrappedException(e);
            }
        };
    }

    public static <S, T> Function<S, Either<Exception, T>> liftWrapped(Function<S, T> wrappedFunction) {
        return v -> {
            try {
                return new Right<>(wrappedFunction.apply(v));
            } catch (WrappedException e) {
                return new Left<>(e.getWrappedException());
            }
        };
    }

    //first wraps the throwable func, then lifts the wrapped func
    public static<S, T> Function<S, Either<Exception, T>> liftThrowable(
            ThrowingFunction<S, T> throwingFunction) {
        return v -> {
            try {
                return new Right<>(wrapThrowable(throwingFunction).apply(v));
            } catch (WrappedException e) {
                return new Left<>(e.getWrappedException());
            }
        };
    }

    public static<I, S, T> Either<S, T> either(I i, Function<I, Boolean> isRight, Function<I, S> toLeft,
                                               Function<I, T> toRight) {
        return eitherFunc(isRight, toLeft, toRight).apply(i);
    }

    public static<I, S, T> Function<I, Either<S, T>> eitherFunc(Function<I, Boolean> isRight, Function<I, S> toLeft,
                                                                Function<I, T> toRight) {
        return (I in) -> {
            if (isRight.apply(in)) {
                return new Right<>(toRight.apply(in));
            } else {
                return new Left<>(toLeft.apply(in));
            }
        };
    }
}

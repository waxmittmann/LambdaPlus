/*
 * Copyright (C) 2015 Covata Limited or its affiliates
 *
 * Information contained within this file cannot be copied,
 * distributed and/or practised without the written consent of
 * Covata Limited or its affiliates.
 */

package lambda.util;

import lambda.either.Either;
import lambda.either.Left;
import lambda.either.Right;

import java.util.function.Function;
import java.util.function.Supplier;

public class LambdaUtils {

    public static <T> T wrapError(ThrowableSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throw new WrappedException(e);
        }
    }

    public static <T> Either<Exception, T> runCatch(ThrowableSupplier<T> supplier) {
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

    public static <S, T> Function<S, T> wrapThrowable(ThrowableFunction<S, T> function) {
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
            ThrowableFunction<S, T> throwableFunction) {
        return v -> {
            try {
                return new Right<>(wrapThrowable(throwableFunction).apply(v));
            } catch (WrappedException e) {
                return new Left<>(e.getWrappedException());
            }
        };
    }
}

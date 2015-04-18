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

public class LambdaUtils {

    public static <T> Either<Exception, T> lift(ThrowableSupplier<T> supplier) {
        try {
            return new Right(supplier.get());
        } catch (Exception e) {
            return new Left(e);
        }
    }
}

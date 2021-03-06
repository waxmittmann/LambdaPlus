/*
 * Copyright (C) 2015 Covata Limited or its affiliates
 *
 * Information contained within this file cannot be copied,
 * distributed and/or practised without the written consent of
 * Covata Limited or its affiliates.
 */

package util.lambdaplus.lambda.util.lambda;

@FunctionalInterface
public interface ThrowingSupplier<S> {
    S get() throws Exception;
}

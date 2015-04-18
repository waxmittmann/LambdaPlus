/*
 * Copyright (C) 2015 Covata Limited or its affiliates
 *
 * Information contained within this file cannot be copied,
 * distributed and/or practised without the written consent of
 * Covata Limited or its affiliates.
 */

package lambda.either;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Either<S, T> {
    public abstract Optional<S> getLeft();
    public abstract Optional<T> getRight();

    public abstract void consume(Consumer<S> leftConsume, Consumer<T> rightConsume);

    public abstract <U> U fold(Function<S, U> leftMap, Function<T, U> rightMap);

    public abstract <U> Either<S, U> flatMap(Function<S, Either<S, U>> leftMap, Function<T, Either<S, U>> rightMap);

    public abstract <U> Either<U, T> flatMapLeft(Function<S, Either<U, T>> rightMap);
    public abstract <U> Either<S, U> flatMapRight(Function<T, Either<S, U>> rightMap);

    public abstract <U> Either<U, T> mapLeft(Function<S, U> rightMap);
    public abstract <U> Either<S, U> mapRight(Function<T, U> rightMap);

    public abstract T getOrDefault(T defaultVal, Consumer<S> leftConsumer);

    public abstract boolean isLeft();

}

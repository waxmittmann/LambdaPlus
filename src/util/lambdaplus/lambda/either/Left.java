/*
 * Copyright (C) 2015 Covata Limited or its affiliates
 *
 * Information contained within this file cannot be copied,
 * distributed and/or practised without the written consent of
 * Covata Limited or its affiliates.
 */

package util.lambdaplus.lambda.either;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Left<S, T> extends Either<S, T> {
    private final S s;

    public Left(S s) {
        this.s = s;
    }

    public S get() {
        return s;
    }

    @Override
    public <U> U fold(Function<S, U> leftMap, Function<T, U> rightMap) {
        return leftMap.apply(s);
    }

    @Override
    public void consume(Consumer<S> leftConsume, Consumer<T> rightConsume) {
        leftConsume.accept(s);
    }

    @Override
    public <U> Either<S, U> mapRight(Function<T, U> rightMap) {
        return new Left(s);
    }

    @Override
    public <U> Either<S, U> flatMapRight(Function<T, Either<S, U>> rightMap) {
        return new Left(s);
    }

    @Override
    public <U> Either<S, U> flatMap(Function<S, Either<S, U>> leftMap, Function<T, Either<S, U>> rightMap) {
        return leftMap.apply(s);
    }

    @Override
    public <U> Either<U, T> mapLeft(Function<S, U> leftMap) {
        return new Left(leftMap.apply(s));
    }

    @Override
    public <U> Either<U, T> flatMapLeft(Function<S, Either<U, T>> leftMap) {
        return leftMap.apply(s);
    }

    @Override
    public T getOrDefault(T defaultVal, Consumer<S> leftConsumer) {
        leftConsumer.accept(s);
        return defaultVal;
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public Optional<S> getLeft() {
        return Optional.of(s);
    }

    @Override
    public Optional<T> getRight() {
        return Optional.empty();
    }
}

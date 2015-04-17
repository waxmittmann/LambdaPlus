/*
 * Copyright (C) 2015 Covata Limited or its affiliates
 *
 * Information contained within this file cannot be copied,
 * distributed and/or practised without the written consent of
 * Covata Limited or its affiliates.
 */

package lambda;

import java.util.function.Consumer;
import java.util.function.Function;

public class Right<S, T> extends Either<S, T> {
    private final T t;

    public Right(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    @Override
    public <U> U fold(Function<S, U> leftMap, Function<T, U> rightMap) {
        return rightMap.apply(t);
    }

    @Override
    public <U> Either<S, U> mapRight(Function<T, U> rightMap) {
        return new Right(rightMap.apply(t));
    }

    @Override
    public <U> Either<S, U> flatMapRight(Function<T, Either<S, U>> rightMap) {
        return rightMap.apply(t);
    }

    @Override
    public <U> Either<S, U> flatMap(Function<S, Either<S, U>> leftMap, Function<T, Either<S, U>> rightMap) {
        return rightMap.apply(t);
    }

    @Override
    public <U> Either<U, T> mapLeft(Function<S, U> rightMap) {
        return new Right(this);
    }

    @Override
    public <U> Either<U, T> flatMapLeft(Function<S, Either<U, T>> rightMap) {
        return new Right(this);
    }

    @Override
    public void consume(Consumer<S> leftConsume, Consumer<T> rightConsume) {
        rightConsume.accept(t);
    }

    @Override
    public T getOrDefault(T defaultVal, Consumer<S> leftConsumer) {
        return t;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

}

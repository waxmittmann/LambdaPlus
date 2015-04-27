package util.lambdaplus.lambda.util.general;

import java.util.Optional;

public class Tuple2<S, T> {
    S first;
    T second;

    public Tuple2(S first, T second) {
        this.first = first;
        this.second = second;
    }

    public S getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

//    public static <S, T> Optional<Tuple2<S, T>> extractFirst(Optional<Tuple2<S, T>> tuple) = {
//            tuple.flatMap((Tuple2<S, T> tuple2) -> Optional.of(tuple2));
//    };

    public static <S, T> Optional<S> extractFirst(Optional<Tuple2<S, T>> tuple) {
        return tuple.flatMap((Tuple2<S, T> tuple2) -> Optional.of(tuple2.getFirst()));
    }

    public static <S, T> Optional<T> extractSecond(Optional<Tuple2<S, T>> tuple) {
        return tuple.flatMap((Tuple2<S, T> tuple2) -> Optional.of(tuple2.getSecond()));
    }

}

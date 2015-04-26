package util.lambdaplus.lambda.util.general;

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
}

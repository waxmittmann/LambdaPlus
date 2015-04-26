package util.lambdaplus.lambda.util.general;

public class Tuple4<S, T, U, V> {
    S first;
    T second;
    U third;
    V fourth;

    public Tuple4(S first, T second, U third, V fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public S getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public U getThird() {
        return third;
    }

    public V getFourth() {
        return fourth;
    }
}

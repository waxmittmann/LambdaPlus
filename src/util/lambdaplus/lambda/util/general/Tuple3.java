package util.lambdaplus.lambda.util.general;

public class Tuple3<S, T, U> {
    S first;
    T second;
    U third;

    public Tuple3(S first, T second, U third) {
        this.first = first;
        this.second = second;
        this.third = third;
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
}

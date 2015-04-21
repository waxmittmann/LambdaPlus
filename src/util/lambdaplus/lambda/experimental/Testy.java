package util.lambdaplus.lambda.experimental;

@FunctionalInterface
public interface Testy<S, T, U> {
    public T doIt(S s);
    public U doOther(S s, T t);
}

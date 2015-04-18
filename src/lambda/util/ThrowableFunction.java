package lambda.util;

@FunctionalInterface
public interface ThrowableFunction<S, T> {
    T apply(S s) throws Exception;
}

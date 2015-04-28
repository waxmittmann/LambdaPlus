package util.lambdaplus.lambda.experimental;

import java.util.Optional;
import java.util.function.Function;

/*
    A a;
    Optional<D> d = new GF(a -> if(blah) return Optional.of(toB(a)) else return Optional.empty());
        .andThen(a -> if(blah) return Optional.of(toB(a)) else return Optional.empty())
        .andThen(b -> if(blah) return Optional.of(toC(a)) else return Optional.empty())
        .andThen(c -> if(blah) return Optional.of(toD(a)) else return Optional.empty())
        .apply(a);
 */
public class OptionalFunction<S, T> {
    public static void main (String [] args) {
        OptionalFunction<Integer, String> guardedFunc =
                first((Integer in) -> {
                    System.out.println("First phase, with " + in);
                    if (in < 10) return Optional.of(in + 5);
                    else return Optional.empty();
                })
                .andThen((Integer in) -> {
                    System.out.println("Second phase, with " + in);
                    if (in % 2 == 0) return Optional.of(in * 2);
                    else return Optional.empty();
                })
                .andThen((Integer in) -> {
                    System.out.println("Third phase, with " + in);
                    if (in % 10 > 3) return Optional.of("Final Result is: " + in + ".");
                    else return Optional.empty();
                });

        for(int i = 0; i < 10; i++) {
            System.out.println(guardedFunc.apply(i));
        }

    }

    protected final Function<S, Optional<T>> convertToNext;

    public OptionalFunction(Function<S, Optional<T>> convertToNext) {
        this.convertToNext = convertToNext;
    }

    public static <S, T> OptionalFunction<S, T> first(Function<S, Optional<T>> convertToNext) {
        return new OptionalFunction<>(convertToNext);
    }

    public <U> OptionalFunction<S, U> andThen(Function<T, Optional<U>> nextConvertToNext) {
        return new OptionalFunction<>((S s) -> {
            Optional<T> t = convertToNext.apply(s);
            return t.flatMap(nextConvertToNext::apply);
        });
    }

    public Optional<T> apply(S s) {
        return convertToNext.apply(s);
    }
}

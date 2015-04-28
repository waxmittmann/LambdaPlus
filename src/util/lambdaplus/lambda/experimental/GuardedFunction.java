package util.lambdaplus.lambda.experimental;

import java.util.Optional;
import java.util.function.Function;

public class GuardedFunction<S, T> extends OptionalFunction<S, T> {
    public static void main (String [] args) {
        GuardedFunction<Integer, String> guardedFunction =
            first((Integer in) -> in * 2, (Integer in) -> in % 2 == 0)
            .andThen((Integer in) -> in + 3, (Integer in) -> in < 16)
            .<String>andThen((Integer in) -> "Result: " + in, (String in) -> in.length() < 10);

        for(int i = 0; i < 10; i++) {
            System.out.println(guardedFunction.apply(i));
        }
    }

    public GuardedFunction(Function<S, T> mapper, Function<T, Boolean> guard) {
        super(combineGuardAndMapper(mapper, guard));
    }

    public GuardedFunction(Function<S, Optional<T>> combined) {
        super(combined);
    }

    private static<S, T> Function<S, Optional<T>> combineGuardAndMapper(Function<S, T> mapper, Function<T, Boolean> guard) {
        return (S s) -> {
            T t = mapper.apply(s);
            if (guard.apply(t)) {
                return Optional.of(t);
            } else {
                return Optional.<T>empty();
            }
        };
    }

    public static <S, T> GuardedFunction<S, T> first(Function<S, T> mapper, Function<T, Boolean> guard) {
        return new GuardedFunction<>(mapper, guard);
    }

    public <U> GuardedFunction<S, U> andThen(Function<T, U> mapper, Function<U, Boolean> guard) {
        return new GuardedFunction<>((S s) -> {
            Optional<T> t = convertToNext.apply(s);
            if (t.isPresent()) {
                U u = mapper.apply(t.get());
                if (guard.apply(u)) {
                    return Optional.of(u);
                }
            }
            return Optional.<U>empty();
        });
    }

}

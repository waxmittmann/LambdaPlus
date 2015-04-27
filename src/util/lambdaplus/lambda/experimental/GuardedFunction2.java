package util.lambdaplus.lambda.experimental;

import java.util.Optional;
import java.util.function.Function;

public class GuardedFunction2<S, T> {
    private final Function<T, Boolean> andThenGuard;
    private final Function<S, Boolean> composeGuard;
    private final Function<S, Optional<T>> func;

//    public GuardedFunction2(Function<S, T> func, Function<S, Boolean> composeGuard, Function<T, Boolean> andThenGuard) {
//        this.func = (S s) -> Optional.of(func.apply(s));
//        this.andThenGuard = andThenGuard;
//        this.composeGuard = composeGuard;
//    }

    public GuardedFunction2(Function<S, Optional<T>> func, Function<S, Boolean> composeGuard, Function<T, Boolean> andThenGuard) {
        this.func = (S s) -> func.apply(s);
        this.andThenGuard = andThenGuard;
        this.composeGuard = composeGuard;
    }

    public static <S, T> GuardedFunction2 fromFunc(Function<S, T> func, Function<S, Boolean> composeGuard,
                                                   Function<T, Boolean> andThenGuard) {
        return new GuardedFunction2<S, T>((S s) -> Optional.of(func.apply(s)), composeGuard, andThenGuard);
    }


//    public <U> GuardedFunction2<S, U> andThen2(Function<T, U> next,
//                                                Function<S, Boolean> nextComposeGuard,
//                                                Function<U, Boolean> nextAndThenGuard) {
//        final Function<S, Optional<U>> wrapFunc = (S s) -> {
//            T result = func.apply(s).get();
//            if (andThenGuard.apply(result)) {
//                return Optional.of(next.apply(result));
//            } else {
//                return Optional.empty();
//            }
//        };
//        return new GuardedFunction2<>(wrapFunc, nextComposeGuard, nextAndThenGuard);
//    }

    public <U> GuardedFunction2<S, U> andThen(Function<T, U> next,
                                              Function<S, Boolean> nextComposeGuard,
                                              Function<U, Boolean> nextAndThenGuard) {
        final Function<S, Optional<U>> wrapFunc = (S s) -> {
            T result = func.apply(s).get();
            if (andThenGuard.apply(result)) {
                return Optional.of(next.apply(result));
            } else {
                return Optional.empty();
            }
        };
        return new GuardedFunction2<>(wrapFunc, nextComposeGuard, nextAndThenGuard);
    }

//    public <U> GuardedFunction2<U, Optional<T>> compose2(Function<U, S> next) {
//        return (U u) -> {
//            S result = next.apply(u);
//            if (composeGuard.apply(result)) {
//                return Optional.of(apply(result));
//            } else {
//                return Optional.empty();
//            }
//        };
//    }

    public Optional<T> apply(S s) {
        return func.apply(s);
    }

}

package util.lambdaplus.lambda.experimental;

import util.lambdaplus.lambda.either.Either;
import util.lambdaplus.lambda.either.Left;
import util.lambdaplus.lambda.either.Right;
import util.lambdaplus.lambda.util.general.Lists;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/*
    public Optional<Employee> transformToEmployee(Person somePerson) {
        Function<Boolean, Boolean> stopWhenFalse = (Boolean bool) -> bool;

        Person somePerson = ...

        GuardedFunction<Person, Boolean> guardedFunc =
            new GuardedFunction((Person p) -> p.getName == "Joseph", stopWhenFalse)
            .andThen((Person p) -> p.getAge() < 30)
            .andThen((Person p) -> p.getExperience().isAtLeast(Experience.HIGH_SCHOOL));

        return guardedFunc.apply(somePerson).map(isOK -> {
            if (isOK) return Optional.of(process(somePerson)) else return Optional.empty()
        });
    }
 */

public class GuardedFunction<S, T> implements Function<S, T> {
    private final Function<S, T> func;
    private Optional<List<Function<T, Boolean>>> guardFunctions = Optional.empty();

    public GuardedFunction(Function<S, T> func, Function<T, Boolean> ... guardFuncs) {
        this.func = func;
        this.guardFunctions = Optional.of(Lists.newArrayList(guardFuncs));
    }

    public T apply(S s) {
        return func.apply(s);
    }

    public <V> GuardedFunction<S, T> compose(GuardedFunction<V, S> before, Function<T, Boolean> ... guardFuncs) {
        Objects.requireNonNull(before);

        return (V v) -> {
            S s = before.apply(v);
            for (Function<T, Boolean> guardFunc : guardFuncs) {

            }
        };


        return (V v) -> {
            Either<L, ? extends T> result = before.apply(v);
            if (result.isLeft()) {
                return new Left<>(result.getLeft().get());
            } else {
                return apply(result.getRightValueOrException());
            }
        };
    }

    /*
        Used to be:
        default <V> EitherFunction<T, L, ? extends V> andThen(EitherFunction<? super R, L, ? extends V> after) {
        but then it won't compile because it wants Either<L, V> returned, not Either<L, ? extends V> for some reason...

        Used to be <T, L, ? extends V> but then will give error on comppilation because apparently it expects ? extends V
        but is getting a V... not sure what that means.
     */
    default <V> GuardedFunction<T, L, V> andThen(GuardedFunction<? super R, L, V> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            Either<L, R> result = apply(t);
            if (result.isLeft()) {
                return new Left<>(result.getLeft().get());
            } else {
                R rightValue = result.getRightValueOrException();
                Either<L, V> finalResult = after.apply(rightValue);
                return finalResult;
            }
        };
    }

    static <T, L> GuardedFunction<T, L, T> identity() {
        return t -> new Right<>(t);
    }
}

package util.lambdaplus.lambda.either;

import java.util.Optional;
import java.util.function.Function;

/*
        FunctionApplier fa =  new FunctionApplier(seed);
        Either<Exception, E> result =
            fa.run(a -> b,
            fa.run(b -> c,
            fa.run(c -> d,
            fa.run(d -> e))));

 */

public class NestedFunctionApplier<L, R, R2, R3>  {
    private Function<R, Either<L, R2>> func;
    private Optional<NestedFunctionApplier<L, R2, R3, ?>> nextFunctionApplier = Optional.empty();
    private Optional<R2> seed = Optional.empty();

    public NestedFunctionApplier(R2 seed) {
        this.seed = Optional.of(seed);
    }

    public NestedFunctionApplier(Function<R, Either<L, R2>> func, NestedFunctionApplier<L, R2, R3, ?> nextFunctionApplier) {
        this.func = func;
        this.nextFunctionApplier = Optional.of(nextFunctionApplier);
    }

    public NestedFunctionApplier(Function<R, Either<L, R2>> func) {
        this.func = func;
    }

    public Either<L, R3> apply(Either<L, R> input) {
        Either<L, R2> result = func.apply(input.getRight().get());
        if (seed.isPresent()) {
            return (Either<L, R3>) nextFunctionApplier.get().apply(new Right<L, R2>(seed.get()));
        } if (nextFunctionApplier.isPresent() && !result.isLeft()) {
            return (Either<L, R3>) nextFunctionApplier.get().apply(new Right<L, R2>(result.getRight().get()));
        } else {
            return new Left(result.getLeft().get());
        }
    }

    public static void main (String [] args) {
//        Either<Exception, Integer> str = new NestedFunctionApplier<>(1,
//            new NestedFunctionApplier<>((Integer nr) -> new Right(nr + 1)).apply(
//            new NestedFunctionApplier<>((Integer nr) -> new Right(nr + 1)).apply(
//            new NestedFunctionApplier<>((Integer nr) -> new Right(nr + 1)).apply())));

//        System.out.println(str);
    }

}

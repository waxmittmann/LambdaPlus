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

public class NestedFunctionApplier2<L, R, R2, R3>  {
    private Function<R, Either<L, R2>> func;
    private Optional<NestedFunctionApplier2<L, R2, R3, ?>> nextFunctionApplier = Optional.empty();
    private Optional<R2> seed = Optional.empty();

    public NestedFunctionApplier2(R2 seed) {
        this.seed = Optional.of(seed);
    }

    public NestedFunctionApplier2(Function<R, Either<L, R2>> func,
                                  NestedFunctionApplier2<L, R2, R3, ?> nextFunctionApplier) {
        this.func = func;
        this.nextFunctionApplier = Optional.of(nextFunctionApplier);

//        Function<Integer, Either<Exception, String>> f1 = i -> new Right<Exception, String>(i.toString());
//        Function<Either<Exception, String>, Either<Exception, Integer>> f1 = i -> new Right(i);

        Function<L, R> f1 = null;
        Function<R, R2> f2 = null;
        Function<R2, R3> f3 = null;
//        f1.compose(f2).compose(f3);

        f3.compose(f2.compose(f1));
        f1.andThen(f2.andThen(f3));

    }

    public NestedFunctionApplier2(Function<R, Either<L, R2>> func) {
        this.func = func;
    }

    public Either<L, R3> apply(Either<L, R> input) {
        Either<L, R2> result = func.apply(input.getRight().get());
        if (seed.isPresent()) {
            return (Either<L, R3>) nextFunctionApplier.get().apply(new Right<L, R2>(seed.get()));
        } if (nextFunctionApplier.isPresent() && !result.isLeft()) {
            return (Either<L, R3>) nextFunctionApplier.get().apply(new Right<L, R2>(result.getRight().get()));
        } else {
            return new Left<L, R3>(result.getLeft().get());
        }
    }

    public static void main (String [] args) {
//        Either<Exception, Integer> str =
//            new NestedFunctionApplier2<>(1,
//            new NestedFunctionApplier2<>((Integer nr) -> new Right(nr + 1)).apply(
//            new NestedFunctionApplier2<>((Integer nr) -> new Right(nr + 1)).apply(
//            new NestedFunctionApplier2<>((Integer nr) -> new Right(nr + 1)).apply())));

//        System.out.println(str);
    }

}

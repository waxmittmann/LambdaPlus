package util.lambdaplus.lambda.either;

import java.util.function.Function;

/*
        FunctionApplier fa =  new FunctionApplier(seed);
        Either<Exception, E> result =
            fa.run(a -> b,
            fa.run(b -> c,
            fa.run(c -> d,
            fa.run(d -> e))));

 */

public class SimpleFunctionApplier<L, R>  {
    Either<L, R> resultOrError;

    public SimpleFunctionApplier(R seed) {
        resultOrError = new Right<>(seed);
    }

    public SimpleFunctionApplier(Either<L, R> resultOrError) {
        this.resultOrError = resultOrError;
    }

    public <R2> SimpleFunctionApplier<L, R2> apply(Function<R, R2> func) {
        return new SimpleFunctionApplier(resultOrError.mapRight(func));
    }

    public Either<L, R> getResult() {
        return resultOrError;
    }

    public static void main (String [] args) {
        SimpleFunctionApplier<Exception, Integer> initial =  new SimpleFunctionApplier<>(1);
        String str = initial
                .apply((nr) -> nr + 1)
                .apply((nr) -> nr * 3)
                .apply((nr) -> nr / 2)
                .apply((nr) -> nr.toString())
                .getResult().getRight().get();

        System.out.println(str);
    }

}

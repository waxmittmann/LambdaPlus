package util.lambdaplus.lambda.integration;

import util.lambdaplus.lambda.either.Either;
import util.lambdaplus.lambda.util.CollectionUtils;
import util.lambdaplus.lambda.util.lambda.LambdaUtils;
import util.lambdaplus.lambda.util.lambda.ThrowingFunction;
import util.lambdaplus.lambda.util.lambda.WrappedException;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class StreamsWithExceptionUsingLift {

    private static final NegativeNumberException negativeNumberException = new NegativeNumberException();

    private static class NegativeNumberException extends Exception {};

    //Todo: Something more realistic here?
    private static double sqrt(double in) throws NegativeNumberException {
        if (in < 0) {
            throw negativeNumberException;
        }
        else
            return Math.sqrt(in);
    }

    /*
        Example for using map and lifting individual results to value or exception
     */
    @Test
    public void shouldLiftMapExceptionsAndValuesCorrectly() {
        List<Double> li = CollectionUtils.list(-1.0, 1.0, 2.0, -3.0);
        List<Either<Exception, Double>> result =
                li.stream()
                .map(nr -> LambdaUtils.runCatch(() -> sqrt(nr)))
                .collect(Collectors.toList());
        assertThat(result.get(0).getLeft().get(), is(negativeNumberException));
        assertThat(result.get(1).getRight().get(), closeTo(1.0, 0.002f));
        assertThat(result.get(2).getRight().get(), closeTo(1.414, 0.002f));
        assertThat(result.get(3).getLeft().get(), is(negativeNumberException));
    }

    /*
        Example for using map and lifting the whole result to eitherFunc a list of values or exception; since the
        WrappedException is immediately thrown, will terminate after the first exception.
     */
    @Test
    public void shouldLiftBeAbleToLiftAnEntireResultToWrappedFailure() {
        List<Double> li = CollectionUtils.list(-1.0, 1.0, 2.0, -3.0);

        //Todo: spy on sqrt to assert that it gets called only once
        Either<Exception, List<Double>> result =
                LambdaUtils.runCatch(() -> li.stream()
                        .map(nr -> LambdaUtils.wrapError(() -> sqrt(nr)))
                        .collect(Collectors.toList()));

        Exception actual = result.getLeft().get();
        assertThat(actual, instanceOf(WrappedException.class));
        assertThat(((WrappedException)actual).getWrappedException(), is(negativeNumberException));
    }

    @Test
    public void shouldLiftBeAbleToLiftExtractAnEntireResultToFailure() {
        List<Double> li = CollectionUtils.list(-1.0, 1.0, 2.0, -3.0);

        //Todo: spy on sqrt to assert that it gets called only once
        Either<Exception, List<Double>> result =
                LambdaUtils.runCatchWrapped(() -> li.stream()
                        .map(nr -> LambdaUtils.wrapError(() -> sqrt(nr)))
                        .collect(Collectors.toList()));

        assertThat(result.getLeft().get(), is(negativeNumberException));
    }

    @Test
    public void shouldLiftBeAbleToLiftAnEntireResultToSuccess() {
        List<Double> li = CollectionUtils.list(1.0, 1.0, 1.0);

        //Todo: spy on sqrt to assert that it gets called three times
        Either<Exception, List<Double>> result =
            LambdaUtils.runCatch(() -> li.stream()
                    .map(nr -> LambdaUtils.wrapError(() -> sqrt(nr)))
                    .collect(Collectors.toList()));

        assertThat(result.getRight().get().size(), is(3));
        assertThat(result.getRight().get(), Matchers.contains(1.0, 1.0, 1.0));
    }

    @Test
    public void shouldWrapFunctionWithLiftMapExceptionsAndValuesCorrectly() {
        List<Double> li = CollectionUtils.list(-1.0, 1.0, 2.0, -3.0);
        ThrowingFunction<Double, Double> throwableSqrt = StreamsWithExceptionUsingLift::sqrt;
        Function<Double, Double> wrappedSqrt = LambdaUtils.wrapThrowable(throwableSqrt);
        Function<Double, Either<Exception, Double>> liftedSqrt = LambdaUtils.liftWrapped(wrappedSqrt);

        List<Either<Exception, Double>> result = li.stream().map(liftedSqrt).collect(Collectors.toList());

        assertThat(result.get(0).getLeft().get(), is(negativeNumberException));
        assertThat(result.get(1).getRight().get(), closeTo(1.0, 0.002f));
        assertThat(result.get(2).getRight().get(), closeTo(1.414, 0.002f));
        assertThat(result.get(3).getLeft().get(), is(negativeNumberException));
    }

    @Test
    public void shouldEitherFunctionDirectlyTakeThrowableFunction() {
        List<Double> li = CollectionUtils.list(-1.0, 1.0, 2.0, -3.0);
        ThrowingFunction<Double, Double> throwableSqrt = StreamsWithExceptionUsingLift::sqrt;
        Function<Double, Either<Exception, Double>> liftedSqrt = LambdaUtils.liftThrowable(throwableSqrt);

        List<Either<Exception, Double>> result = li.stream().map(liftedSqrt).collect(Collectors.toList());

        assertThat(result.get(0).getLeft().get(), is(negativeNumberException));
        assertThat(result.get(1).getRight().get(), closeTo(1.0, 0.002f));
        assertThat(result.get(2).getRight().get(), closeTo(1.414, 0.002f));
        assertThat(result.get(3).getLeft().get(), is(negativeNumberException));
    }

}

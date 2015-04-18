package lambda.integration;

import lambda.either.Either;
import lambda.util.CollectionUtils;
import lambda.util.LambdaUtils;
import lambda.util.ThrowableFunction;
import lambda.util.WrappedException;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static lambda.util.LambdaUtils.lift;
import static lambda.util.LambdaUtils.liftExtract;
import static lambda.util.LambdaUtils.wrapError;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.number.IsCloseTo.closeTo;
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
                .map(nr -> lift(() -> sqrt(nr)))
                .collect(Collectors.toList());
        assertThat(result.get(0).getLeft().get(), is(negativeNumberException));
        assertThat(result.get(1).getRight().get(), closeTo(1.0, 0.002f));
        assertThat(result.get(2).getRight().get(), closeTo(1.414, 0.002f));
        assertThat(result.get(3).getLeft().get(), is(negativeNumberException));
    }

    /*
        Example for using map and lifting the whole result to either a list of values or exception; since the
        WrappedException is immediately thrown, will terminate after the first exception.
     */
    @Test
    public void shouldLiftBeAbleToLiftAnEntireResultToWrappedFailure() {
        List<Double> li = CollectionUtils.list(-1.0, 1.0, 2.0, -3.0);

        //Todo: spy on sqrt to assert that it gets called only once
        Either<Exception, List<Double>> result =
                lift(() -> li.stream()
                        .map(nr -> wrapError(() -> sqrt(nr)))
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
                liftExtract(() -> li.stream()
                    .map(nr -> wrapError(() -> sqrt(nr)))
                    .collect(Collectors.toList()));

        assertThat(result.getLeft().get(), is(negativeNumberException));
    }

    @Test
    public void shouldLiftBeAbleToLiftAnEntireResultToSuccess() {
        List<Double> li = CollectionUtils.list(1.0, 1.0, 1.0);

        //Todo: spy on sqrt to assert that it gets called three times
        Either<Exception, List<Double>> result =
            lift(() -> li.stream()
                .map(nr -> wrapError(() -> sqrt(nr)))
                .collect(Collectors.toList()));

        assertThat(result.getRight().get().size(), is(3));
        assertThat(result.getRight().get(), Matchers.contains(1.0, 1.0, 1.0));
    }

    @Test
    public void shouldWrapFunctionWithLiftMapExceptionsAndValuesCorrectly() {
        List<Double> li = CollectionUtils.list(-1.0, 1.0, 2.0, -3.0);
        ThrowableFunction<Double, Double> throwableSqrt = StreamsWithExceptionUsingLift::sqrt;
        Function<Double, Double> wrappedSqrt = LambdaUtils.wrapFunction(throwableSqrt);
        Function<Double, Either<Exception, Double>> eitheredSqrt = LambdaUtils.eitherFunction(wrappedSqrt);

        List<Either<Exception, Double>> result = li.stream().map(eitheredSqrt).collect(Collectors.toList());

        assertThat(result.get(0).getLeft().get(), is(negativeNumberException));
        assertThat(result.get(1).getRight().get(), closeTo(1.0, 0.002f));
        assertThat(result.get(2).getRight().get(), closeTo(1.414, 0.002f));
        assertThat(result.get(3).getLeft().get(), is(negativeNumberException));
    }

    @Test
    public void shouldEitherFunctionDirectlyTakeThrowableFunction() {
        List<Double> li = CollectionUtils.list(-1.0, 1.0, 2.0, -3.0);
        ThrowableFunction<Double, Double> throwableSqrt = StreamsWithExceptionUsingLift::sqrt;
        Function<Double, Either<Exception, Double>> eitheredSqrt = LambdaUtils.eitherFunction(throwableSqrt);

        List<Either<Exception, Double>> result = li.stream().map(eitheredSqrt).collect(Collectors.toList());

        assertThat(result.get(0).getLeft().get(), is(negativeNumberException));
        assertThat(result.get(1).getRight().get(), closeTo(1.0, 0.002f));
        assertThat(result.get(2).getRight().get(), closeTo(1.414, 0.002f));
        assertThat(result.get(3).getLeft().get(), is(negativeNumberException));
    }

}

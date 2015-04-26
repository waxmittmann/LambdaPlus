package util.lambdaplus.lambda.util;

import util.lambdaplus.lambda.either.Either;
import org.junit.Test;
import util.lambdaplus.lambda.util.lambda.LambdaUtils;
import util.lambdaplus.lambda.util.lambda.ThrowingFunction;
import util.lambdaplus.lambda.util.lambda.ThrowingSupplier;
import util.lambdaplus.lambda.util.lambda.WrappedException;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LambdaUtilsTest {

    @Test
    public void liftShouldProduceLeftWhenException() throws Exception {
        final Exception exception = new Exception("Bad");
        ThrowingSupplier<Integer> supplier = () -> {
            throw exception;};

        Either<Exception, Integer> either = LambdaUtils.runCatch(supplier);

        assertThat(either.getLeft().get(), is(exception));
    }

    @Test
    public void liftShouldProduceRightWhenNoException() throws Exception {
        final int val = 1;
        ThrowingSupplier<Integer> supplier = () -> val;

        Either<Exception, Integer> either = LambdaUtils.runCatch(supplier);

        assertThat(either.getRight().get(), is(1));
    }

    //Todo: Test the function lifting etc
    @Test
    public void shouldLiftExtractGetThrownExceptionDirectly() {
        final CheckedTestException checkedException = new CheckedTestException();
        Supplier<Integer> supplier = () -> {
            throw new WrappedException(checkedException);
        };

        Either<Exception, Integer> either = LambdaUtils.runCatchWrapped(supplier);

        assertThat(either.isLeft(), is(true));
        assertThat(either.getLeft().get(), is(checkedException));
    }

    @Test
    public void shouldWrapFunctionConvertThrowableFunctionToFunctionThatProducesCorrectValue() {
        final CheckedTestException checkedException = new CheckedTestException();
        ThrowingFunction<Integer, Integer> throwingFunction = (in) -> {
            if (in == 0)
                return 1;
            else
                throw checkedException;
        };
        Function<Integer, Integer> function = LambdaUtils.wrapThrowable(throwingFunction);

        Integer result = function.apply(0);

        assertThat(result, is(1));
    }

    @Test
    public void shouldWrapFunctionConvertThrowableFunctionToFunctionThatWrapsExceptions() {
        final CheckedTestException checkedException = new CheckedTestException();
        ThrowingFunction<Integer, Integer> throwingFunction = (in) -> {
            if (in == 0)
                return 1;
            else
                throw checkedException;
        };
        Function<Integer, Integer> function = LambdaUtils.wrapThrowable(throwingFunction);

        try {
            function.apply(1);
        } catch(WrappedException e) {
            assertThat(e.getWrappedException(), is(checkedException));
        }
    }

    //Todo: RENAME
    @Test
    public void shouldDoIt() {
        final CheckedTestException checkedException = new CheckedTestException();
        ThrowingFunction<Integer, Integer> throwingFunction = (in) -> {
            if (in == 0)
                return 1;
            else
                throw checkedException;
        };
        Function<Integer, Either<Exception, Integer>> eitherFunction = LambdaUtils.liftThrowable(
                throwingFunction);

        Either<Exception, Integer> result = eitherFunction.apply(0);

        assertThat(result.isLeft(), is(false));
        assertThat(result.getRight().get(), is(1));
    }

    @Test
    public void shouldDoIt2() {
        final CheckedTestException checkedException = new CheckedTestException();
        ThrowingFunction<Integer, Integer> throwingFunction = (in) -> {
            if (in == 0)
                return 1;
            else
                throw checkedException;
        };
        Function<Integer, Either<Exception, Integer>> eitherFunction = LambdaUtils.liftThrowable(
                throwingFunction);

        Either<Exception, Integer> result = eitherFunction.apply(1);

        assertThat(result.isLeft(), is(true));
        assertThat(result.getLeft().get(), is(checkedException));
    }

}
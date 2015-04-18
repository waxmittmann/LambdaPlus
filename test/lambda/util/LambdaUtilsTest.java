package lambda.util;

import lambda.either.Either;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class LambdaUtilsTest {

    @Test
    public void liftShouldProduceLeftWhenException() throws Exception {
        final Exception exception = new Exception("Bad");
        ThrowableSupplier<Integer> supplier = () -> {
            throw exception;};

        Either<Exception, Integer> either = LambdaUtils.lift(supplier);

        assertThat(either.getLeft().get(), is(exception));
    }

    @Test
    public void liftShouldProduceRightWhenNoException() throws Exception {
        final int val = 1;
        ThrowableSupplier<Integer> supplier = () -> val;

        Either<Exception, Integer> either = LambdaUtils.lift(supplier);

        assertThat(either.getRight().get(), is(1));
    }

    //Todo: Test the function lifting etc
}
package util.lambdaplus.lambda.integration;

import org.hamcrest.Matchers;
import org.junit.Test;
import util.lambdaplus.lambda.either.Either;
import util.lambdaplus.lambda.either.EitherFunction;
import util.lambdaplus.lambda.util.LambdaUtils;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertThat;

public class EitherFunctionWithLift {

    //Todo: Create an eitherFunction subclass that automatically wraps thrown exceptions
    @Test
    public void testEitherFunctionWithLiftWillCatchExceptionAsEither() {
        EitherFunction<Integer, Exception, String> func =
            EitherFunction.<Integer, Exception>identity()
                .andThen(nr -> LambdaUtils.runCatch(() -> nr + 5))
                .andThen(nr -> LambdaUtils.runCatch(() -> 10 / nr))
                    .andThen(nr -> LambdaUtils.runCatch(() -> "_" + nr + "_"));

        Either<Exception, String> result = func.apply(-5);

        result.consume(
                l -> assertThat(l, Matchers.instanceOf(ArithmeticException.class)),
                r -> fail("Should throw exception")
        );
    }
}

package util.lambdaplus.lambda.integration;

import org.hamcrest.Matchers;
import org.junit.Test;
import util.lambdaplus.lambda.either.Either;
import util.lambdaplus.lambda.either.EitherFunction;
import util.lambdaplus.lambda.util.lambda.LambdaUtils;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class EitherFunctionWithLift {

    //Todo: Create an eitherFunction subclass that automatically wraps thrown exceptions
    @Test
    public void testEitherFunctionWithLiftWillCatchExceptionAsEither() {
        EitherFunction<Integer, Exception, String> func =
            EitherFunction.<Integer, Exception>identity()
                .andThen2(nr -> LambdaUtils.runCatch(() -> nr + 5))
                .andThen2(nr -> LambdaUtils.runCatch(() -> 10 / nr))
                    .andThen2(nr -> LambdaUtils.runCatch(() -> "_" + nr + "_"));

        Either<Exception, String> result = func.apply(-5);

        result.consume(
                l -> assertThat(l, Matchers.instanceOf(ArithmeticException.class)),
                r -> fail("Should throw exception")
        );
    }
}

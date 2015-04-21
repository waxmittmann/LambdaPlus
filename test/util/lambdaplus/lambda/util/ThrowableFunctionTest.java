package util.lambdaplus.lambda.util;

import org.junit.Test;
import util.lambdaplus.lambda.either.EitherFunction;
import util.lambdaplus.lambda.either.Right;

import static junit.framework.TestCase.fail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ThrowableFunctionTest {

    @Test
    public void shouldComposeWorkLikeEitherFunctionWhenNoException() throws Exception {
        EitherFunction<Integer, Exception, String> combined
                = EitherFunction.<Integer, Exception>identity()
                .andThen((Integer nr) -> new Right<>(nr * 2)) // (1) 5 * 2 = 10
                .andThen((Integer nr) -> new Right<>(nr + 2)) // (2) 10 + 2 = 12
                .andThen((Integer nr) -> new Right<>("_" + nr + "_")); // (3) _12_

        combined.apply(5).consume(
                e -> fail("Exception thrown"), r -> assertThat(r, is("_12_"))
        );

    }

    @Test
    public void testAndThen() throws Exception {
        ThrowableFunction<Integer, String> combined
                = ThrowableFunction.<Integer>identity()
                .andThen((Integer nr) -> (nr * 2)) // (1) 5 * 2 = 10
                .andThen((Integer nr) -> (nr + 2)) // (2) 10 + 2 = 12
                .andThen((Integer nr) -> ("_" + nr + "_")); // (3) _12_

        combined.apply(5).consume(
                e -> fail("Exception thrown"), r -> assertThat(r, is("_12_"))
        );
    }
}
package util.lambdaplus.lambda.either;

import org.junit.Test;
import util.lambdaplus.lambda.util.ThrowableFunction;

import static junit.framework.TestCase.fail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LiftableFunctionTest {

    @Test
    public void shouldComposeWorkSameAsEitherFunctionWhenNoErrors() throws Exception {
        final Exception exception = new Exception("Bad");
//        EitherFunction<Integer, Exception, Integer> combined
//                = LiftableFunction.<Integer>identity().compose((Integer nr)-> nr * 2);

        EitherFunction<Integer, Exception, Integer> combined = ThrowableFunction.<Integer>identity()
                .andThen((Integer nr) -> (nr * 2));
        EitherFunction<Integer, Exception, Integer> combined2 = combined.andThen((Integer nr) -> (nr + 2));

        EitherFunction<Integer, Exception, String> combined
                = ThrowableFunction.<Integer>identity()
                .andThen((Integer nr) -> (nr * 2)) // (1) 5 * 2 = 10
                .andThen((Integer nr) -> (nr + 2)) // (2) 10 + 2 = 12
                .andThen((Integer nr) -> ("_" + nr + "_")); // (3) _12_

        combined.apply(5).consume(
                e -> fail("Exception thrown"), r -> assertThat(r, is("_12_"))
        );

    }
//
//    @Test
//    public void testAndThen() throws Exception {
//
//    }
//
//    @Test
//    public void testIdentity() throws Exception {
//
//    }
}
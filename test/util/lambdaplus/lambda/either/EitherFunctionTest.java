package util.lambdaplus.lambda.either;

import org.junit.Test;

import static junit.framework.TestCase.fail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EitherFunctionTest {
    @Test
    public void testApplyForLeft() throws Exception {
        EitherFunction<Integer, Float, String> eitherFunc = (i) -> new Left((new Float(i) + 0.05f));

        Either<Float, String> result = eitherFunc.apply(2);

        result.consume(
                l -> assertThat(l, is(2.05f)),
                r -> fail("Should not return right")
        );
    }

    @Test
    public void testApplyForRight() throws Exception {
        EitherFunction<Integer, Exception, String> eitherFunc = (i) -> new Right(("_" + i + "_"));

        Either<Exception, String> result = eitherFunc.apply(2);

        result.consume(
                e -> fail("Should not throw exception"),
                r -> assertThat(r, is("_2_"))
        );
    }

    @Test
    public void testComposeReturnsLeftOnFailure() throws Exception {
        final Exception exception = new Exception("Bad");
        EitherFunction<Integer, Exception, String> combined
                = EitherFunction.<String, Exception>identity()
                .compose((Integer nr) -> {
                    fail("Should never get here");
                    return new Right<>("_" + nr + "_");
                })
                .compose((Integer nr) -> new Left<>(exception)) //Hmm, this one doesn't need params, even though andThen does??
                .compose((Integer nr) -> new Right<>(nr * 2));

        combined.apply(5).consume(
                e -> assertThat(e, is(exception)),
                r -> fail("Exception not returned")
        );
    }

    @Test
    public void testAndThenCreatesWorkingLeftToRightChain() throws Exception {
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
    public void testAndThenReturnsLeftWhenFailure() throws Exception {
        final Exception exception = new Exception("Terrible!");
        EitherFunction<Integer, Exception, String> combined
                = EitherFunction.<Integer, Exception>identity()
                .andThen((Integer nr) -> new Right<>(nr * 2))
                .andThen((Integer nr) -> new Left<Exception, Integer>(exception)) // Fail here (needs the type params for some reason)
                .andThen((Integer nr) -> {
                    fail("Should never get here");
                    return new Right<>("_" + nr + "_");
                });

        combined.apply(5).consume(
                e -> assertThat(e, is(exception)),
                r -> fail("Exception not returned")
        );
    }

    @Test
    public void testIdentity() throws Exception {
        EitherFunction<Integer, Exception, Integer> identityFunc = EitherFunction.identity();
        int in = 5;

        Either<Exception, Integer> out = identityFunc.apply(in);

        out.consume(
            e -> fail("Exception was thrown"),
            r -> assertThat(r, is(in))
        );
    }
}
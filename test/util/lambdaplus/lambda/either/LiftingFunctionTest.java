package util.lambdaplus.lambda.either;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LiftingFunctionTest {

    @Test
    public void testApplyAppliesWrappedFunction() throws Exception {
        LiftingFunction<Integer, Integer> liftableFunc
                = new LiftingFunction<>((Integer a) -> a + 1);

        Integer result = liftableFunc.apply(1).getRight().get();

        assertThat(result, is(2));
    }

    @Test
    public void testAndThenChainsCorrectly() throws Exception {
        LiftingFunction<Integer, String> liftableFunc
                = new LiftingFunction<>((Integer a) -> a + 1)
                    .andThen((Integer b) -> b * 2)
                    .andThen((Integer b) -> "_" + b + "_");

        String result = liftableFunc.apply(1).getRight().get();

        assertThat(result, is("_4_"));
    }

    @Test
    public void testAndThenFailsCorrectly() throws Exception {
        final Exception expectedException = new Exception("Bad");
        LiftingFunction<Integer, String> liftableFunc
                = new LiftingFunction<>((Integer a) -> a + 1)
                .andThen((Integer b) -> {
                    if (true) {
                        throw expectedException;
                    }
                    return b * 2;
                })
                .andThen((Integer b) -> "_" + b + "_");

        Exception result = liftableFunc.apply(1).getLeft().get();

        assertThat(result, is(expectedException));
    }

    @Test
    public void testComposeChainsCorrectly() throws Exception {
        LiftingFunction<Integer, String> liftableFunc
                = new LiftingFunction<>((Integer a) -> "_" + a + "_")
                .compose((Integer b) -> b * 2)
                .compose((Integer b) -> b + 1);

        String result = liftableFunc.apply(1).getRight().get();

        assertThat(result, is("_4_"));
    }

    @Test
    public void testComposeFailsCorrectly() throws Exception {
        final Exception exception = new Exception("Bad!");
        LiftingFunction<Integer, String> liftableFunc
                = new LiftingFunction<>((Integer a) -> "_" + a + "_")
                .compose((Integer b) -> {
                    if (true) {
                        throw exception;
                    }
                    return b * 2;
                })
                .compose((Integer b) -> b + 1);

        Exception result = liftableFunc.apply(1).getLeft().get();

        assertThat(result, is(exception));
    }
}
package util.lambdaplus.lambda.either;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LiftableFunctionTest {

    @Test
    public void testApplyAppliesWrappedFunction() throws Exception {
        LiftableFunction<Integer, Integer> liftableFunc
                = new LiftableFunction<>((Integer a) -> a + 1);

        Integer result = liftableFunc.apply(1).getRight().get();

        assertThat(result, is(2));
    }

    @Test
    public void testAndThenChainsCorrectly() throws Exception {
        LiftableFunction<Integer, String> liftableFunc
                = new LiftableFunction<>((Integer a) -> a + 1)
                    .andThen((Integer b) -> b * 2)
                    .andThen((Integer b) -> "_" + b + "_");

        String result = liftableFunc.apply(1).getRight().get();

        assertThat(result, is("_4_"));
    }

    @Test
    public void testAndThenFailsCorrectly() throws Exception {
        final Exception expectedException = new Exception("Bad");
        LiftableFunction<Integer, String> liftableFunc
                = new LiftableFunction<>((Integer a) -> a + 1)
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
        LiftableFunction<Integer, String> liftableFunc
                = new LiftableFunction<>((Integer a) -> "_" + a + "_")
                .compose((Integer b) -> b * 2)
                .compose((Integer b) -> b + 1);

        String result = liftableFunc.apply(1).getRight().get();

        assertThat(result, is("_4_"));
    }

    @Test
    public void testComposeFailsCorrectly() throws Exception {
        final Exception exception = new Exception("Bad!");
        LiftableFunction<Integer, String> liftableFunc
                = new LiftableFunction<>((Integer a) -> "_" + a + "_")
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
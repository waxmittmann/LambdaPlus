package util.lambdaplus.functionalif;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Function;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

//Todo: more tests to come here
public class IfTest {

    @Test
    public void ifAppliesBodyWhenTrue() {
        Function<Integer, Boolean> test = (Integer i) -> i == 1;
        Function<Integer, Integer> map = (Integer i) -> i = 2;
        If<Integer> _if = new If(test, map);

        Optional<Integer> result = _if.apply(1);

        assertThat(result.get(), is(2));
    }

    @Test
    public void ifDoesntApplyBodyWhenFalse() {
        Function<Integer, Boolean> test = (Integer i) -> i == 1;
        Function<Integer, Integer> map = (Integer i) -> i = 2;
        If<Integer> _if = new If(test, map);

        Optional<Integer> result = _if.apply(2);

        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void ifAppliesBodyWhenTrueWithFlatMap() {
        Function<Integer, Optional<Integer>> func = (Integer i) -> {
            if (i == 1) {
                return Optional.of(2);
            }
            return Optional.empty();
        };
        If<Integer> _if = new If(func);

        Optional<Integer> result = _if.apply(1);

        assertThat(result.get(), is(2));
    }

    @Test
    public void ifDoesntApplyBodyWhenFalseWithFlatMap() {
        Function<Integer, Optional<Integer>> func = (Integer i) -> {
            if (i == 1) {
                return Optional.of(2);
            }
            return Optional.empty();
        };
        If<Integer> _if = new If(func);

        Optional<Integer> result = _if.apply(2);

        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void elseIfAppliesWhenIfDoesnt() {
        If<Integer> _if = new If<>((i) -> i == 1, (i) -> i = 3);
        If<Integer> _else_if = new If<>((i) -> i == 2, (i) -> i = 4);
        If<Integer> composite = _if._elseIf(_else_if);

        Optional<Integer> result = composite.apply(2);

        assertThat(result.get(), is(4));
    }

    @Test
    public void elseIfDoesntApplyWhenIfDoes() {
        If<Integer> _if = new If<>((i) -> i == 1, (i) -> i = 3);
        If<Integer> _else_if = new If<>((i) -> i == 2, (i) -> i = 4);
        If<Integer> composite = _if._elseIf(_else_if);

        Optional<Integer> result = composite.apply(1);

        assertThat(result.get(), is(3));
    }

    @Test
    public void neitherIfNorElseIfApplyWhenConditionsAreFalse() {
        If<Integer> _if = new If<>((i) -> i == 1, (i) -> i = 3);
        If<Integer> _else_if = new If<>((i) -> i == 2, (i) -> i = 4);
        If<Integer> composite = _if._elseIf(_else_if);

        Optional<Integer> result = composite.apply(3);

        assertThat(result, is(Optional.empty()));
    }

    @Test
    public void elseAppliesWhenIfDoesnt() {
        If<Integer> _if = new If<>((i) -> i == 1, (i) -> i = 2);
        If<Integer> composite = _if._else((i) -> i = 3);

        Optional<Integer> result = composite.apply(2);

        assertThat(result.get(), is(3));
    }

    @Test
    public void elseDoesntApplyWhenIfDoes() {
        If<Integer> _if = new If<>((i) -> i == 1, (i) -> i = 2);
        If<Integer> composite = _if._else((i) -> i = 3);

        Optional<Integer> result = composite.apply(1);

        assertThat(result.get(), is(2));
    }

    @Ignore
    @Test
    public void innerIfsWorkProperly() {
        //Todo: Write these if we keep innerIfs
    }

    @Ignore
    @Test
    public void insideIfsShouldGetAppliedWhenOuterIfIsApplied() {
        If<Integer> _if1 = new If<>((i) -> i == 1, (i) -> i = 101);
        If<Integer> _if2 = new If<>((i) -> i == 2, (i) -> i = 102);
        If<Integer> _inner_if2_1 = new If<>((i) -> i == 2, (i) -> i = 102);
        If<Integer> _if3 = new If<>((i) -> i == 3, (i) -> i = 103);


    }

    @Ignore
    @Test
    public void insideIfsShouldNotBeAppliedWhenOuterIfIsNotApplied() {

    }

    @Ignore
    @Test
    public void ifFollowingInnerIfsShouldNotBeApplied() {

    }

    @Ignore
    @Test
    public void innerIfBlockShouldExecuteBeforeFollowingInnerIf() {

    }

    @Ignore
    @Test
    public void followingInnerIfShouldExecuteWhenInnerIfDoesNot() {

    }
}
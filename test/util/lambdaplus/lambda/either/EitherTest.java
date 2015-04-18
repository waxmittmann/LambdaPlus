package util.lambdaplus.lambda.either;

import org.junit.Test;
import util.lambdaplus.lambda.util.Store;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EitherTest {

    private enum Bi {
        LEFT, RIGHT, NEITHER
    }

    @Test
    public void testConsumeLeftConsumesLeft() {
        Either leftEither = new Left(5);

        //Object counter = Mockito.mock(Object.class);
        Store<Bi> store = new Store();
        leftEither.consume(
                (_x) -> store.set(Bi.LEFT),
                (_x) -> store.set(Bi.RIGHT)
        );

        assertThat(store.get(), is(Bi.LEFT));
    }

    @Test
    public void testConsumeRightConsumesRight() {
        Either rightEither = new Right(5);

        Store<Bi> store = new Store();
        rightEither.consume(
                (_x) -> store.set(Bi.LEFT),
                (_x) -> store.set(Bi.RIGHT)
        );

        assertThat(store.get(), is(Bi.RIGHT));
    }

    @Test
    public void testFoldFoldsLeft() {
        Either<Integer, Integer> leftEither = new Left(1);
        Integer result = leftEither.fold(
            (l) -> l+1,
            (r) -> r+2
        );

        assertThat(result, is(2));
    }

    @Test
    public void testFoldFoldsRight() {
        Either<Integer, Integer> rightEither = new Right(1);

        Integer result = rightEither.fold(
                (l) -> l+1,
                (r) -> r+2
        );

        assertThat(result, is(3));
    }

    @Test
    public void testMapLeftMapsLeft() {
        Either<Integer, Integer> leftEither = new Left(1);

        leftEither = leftEither.mapLeft(
                l -> l + 1
        );

        assertThat(leftEither.isLeft(), is(true));
        assertThat(leftEither.getLeft().get(), is(2));
    }

    @Test
    public void testMapRightMapsRight() {
        Either<Integer, Integer> rightEither = new Right(1);

        rightEither = rightEither.mapRight(
            r -> r + 1
        );

        assertThat(rightEither.isLeft(), is(false));
        assertThat(rightEither.getRight().get(), is(2));
    }

    @Test
    public void testFlatMapFlatMapsLeft() {
        Either<Bi, Bi> leftEither = new Left(Bi.LEFT);

        leftEither = leftEither.flatMap(
                (l) -> new Left(l),
                (r) -> new Right(Bi.NEITHER)
        );

        assertThat(leftEither.isLeft(), is(true));
        assertThat(leftEither.getLeft().get(), is(Bi.LEFT));
    }

    @Test
    public void testFlatMapFlatMapsRight() {
        Either<Bi, Bi> rightEither = new Right<>(Bi.RIGHT);

        rightEither = rightEither.flatMap(
                (l) -> new Left(Bi.NEITHER),
                (r) -> new Right(r)
        );

        assertThat(rightEither.isLeft(), is(false));
        assertThat(rightEither.getRight().get(), is(Bi.RIGHT));
    }

    @Test
    public void testFlatMapLeftFlatMapsLeft() {
        Either<Integer, Integer> leftEither = new Left(1);

        leftEither = leftEither.flatMapLeft(
                (l) -> new Left(l + 1)
        );

        assertThat(leftEither.isLeft(), is(true));
        assertThat(leftEither.getLeft().get(), is(2));
    }

    @Test
    public void testFlatMapLeftFlatDoesNotFlatMapRight() {
        Either<Integer, Integer> rightEither = new Right(1);

        rightEither = rightEither.flatMapLeft(
                (l) -> new Left(l+1)
        );

        assertThat(rightEither.isLeft(), is(false));
        assertThat(rightEither.getRight().get(), is(1));
    }

    @Test
    public void testFlatMapRightFlatMapsRight() {
        Either<Integer, Integer> rightEither = new Right<>(1);

        rightEither = rightEither.flatMapRight(
                (r) -> new Right(r + 1)
        );

        assertThat(rightEither.isLeft(), is(false));
        assertThat(rightEither.getRight().get(), is(2));
    }

    @Test
    public void testFlatMapRightDoesNotFlatMapsLeft() {
        Either<Integer, Integer> leftEither = new Left<>(1);

        leftEither = leftEither.flatMapRight(
                (r) -> new Right(r+1)
        );

        assertThat(leftEither.isLeft(), is(true));
        assertThat(leftEither.getLeft().get(), is(1));
    }

    @Test
    public void getOrDefaultShouldGetWhenRight() {
        Either<Integer, Integer> rightEither = new Right<>(1);

        Integer result = rightEither.getOrDefault(0, l -> {throw new RuntimeException("Should not be called");});

        assertThat(result, is(1));
    }

    @Test
    public void getOrDefaultShouldDefaultAndConsumeWhenLeft() {
        Either<Integer, Integer> leftEither = new Left<>(1);

        Store<Integer> store = new Store();
        Integer result = leftEither.getOrDefault(0, l -> store.set(l));

        assertThat(result, is(0));
        assertThat(store.get(), is(1));
    }

}
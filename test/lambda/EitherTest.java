package lambda;

import org.junit.Test;
import util.Store;

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
    public void testMapLeftMapsLeft() {
        Either<Bi, Bi> leftEither = new Left(Bi.LEFT);

        leftEither = leftEither.flatMap(
                (l) -> new Left(l),
                (r) -> new Right(Bi.NEITHER)
        );

        assertThat(leftEither.isLeft(), is(true));
        assertThat(leftEither.getLeft().get(), is(Bi.LEFT));
    }

    @Test
    public void testMapRightMapsRight() {
        Either<Bi, Bi> rightEither = new Right<>(Bi.RIGHT);

        rightEither = rightEither.flatMap(
                (l) -> new Left(Bi.NEITHER),
                (r) -> new Right(r)
        );

        assertThat(rightEither.isLeft(), is(false));
        assertThat(rightEither.getRight().get(), is(Bi.RIGHT));
    }
}
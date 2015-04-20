package util.lambdaplus.lambda.experimental;

import util.lambdaplus.lambda.either.Either;
import util.lambdaplus.lambda.either.Left;
import util.lambdaplus.lambda.either.Right;

import java.util.Objects;

@FunctionalInterface
public interface EitherFunction<T, L, R> {

    public static void main (String [] args) {
        {
            EitherFunction<Integer, Exception, String> combined
                    = EitherFunction.<Integer, Exception>identity()
                    .andThen((Integer nr) -> new Right<>(nr * 2))
                    .andThen((Integer nr) -> new Right<>(nr + 2))
                    .andThen((Integer nr) -> new Right<>("_" + nr + "_"));

            combined.apply(5).consume(
                    System.out::println,
                    System.out::println
            );
        }

        {
            EitherFunction<Integer, Exception, String> combined
                    = EitherFunction.<String, Exception>identity()
                    .compose((String str) -> new Right<>(str.substring(3)))
                    .compose((Integer nr) -> new Right<>("_" + nr + "_"))
                    .compose((Integer nr) -> new Right<>(nr * 23348));

            combined.apply(5).consume(
                    System.out::println,
                    System.out::println
            );
        }


    }

    Either<L, R> apply(T t);

    default <V> EitherFunction<V, L, R> compose(EitherFunction<? super V, L, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> {
            Either<L, ? extends T> result = before.apply(v);
            if (result.isLeft()) {
                return new Left<>(result.getLeft().get());
            } else {
                return apply(result.getRightValueOrException());
            }
        };
    }

    /*
        Used to be:
        default <V> EitherFunction<T, L, ? extends V> andThen(EitherFunction<? super R, L, ? extends V> after) {
        but then it won't compile because it wants Either<L, V> returned, not Either<L, ? extends V> for some reason...

        Used to be <T, L, ? extends V> but then will give error on comppilation because apparently it expects ? extends V
        but is getting a V... not sure what that means.
     */
    default <V> EitherFunction<T, L, V> andThen(EitherFunction<? super R, L, V> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            Either<L, R> result = apply(t);
            if (result.isLeft()) {
                return new Left<>(result.getLeft().get());
            } else {
                R rightValue = result.getRightValueOrException();
                Either<L, V> finalResult = after.apply(rightValue);
                return finalResult;
            }
        };
    }

    static <T, L> EitherFunction<T, L, T> identity() {
        return t -> new Right<>(t);
    }
}

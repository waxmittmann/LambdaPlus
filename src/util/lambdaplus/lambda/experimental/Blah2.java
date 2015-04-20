package util.lambdaplus.lambda.experimental;

import util.lambdaplus.lambda.either.Either;
import util.lambdaplus.lambda.either.Left;
import util.lambdaplus.lambda.either.Right;

import java.util.function.Function;

/*
    Blah(
 */
@FunctionalInterface
public interface Blah2<S, L, R> {

    public static void main (String [] args) {
        Function<Integer, Integer> f1 = a -> a;
        Function<Integer, String> f2 = a -> a + "_";
        Function<String, StringBuffer> f3 = a -> new StringBuffer(a);

        Function<Integer, String> f4 = f1.andThen(f2);
        Function<Integer, StringBuffer> f5 = f4.andThen(f3);

//
//        Blah2<Integer, Exception, Integer> blah1 = (Integer i) -> new Right(i + 1);
//        Blah2<Integer, Exception, Integer> blah2 = (Integer i) -> new Right(i * 1);
//        Blah2<Integer, Exception, String> blah3 = (Integer i) -> new Right("_" + i + "_");
//        Function<Integer, Either<Exception, String>> compoundFunc =
//                blah1.create(blah2.create(blah3.create()));
//
//        Function<Integer, Either<Exception, String>> compoundFunc2 =
//            ((Blah2<Integer, Exception, Integer>) (Integer i) -> new Right(i + 1))
//            .create(((Blah2<Integer, Exception, Integer>) (Integer i) -> new Right(i * 1))
//            .create(((Blah2<Integer, Exception, String>) (Integer i) -> new Right("_" + i + "_"))
//            .create()));
//
//        Function<Integer, Either<Exception, String>> compoundFunc3 =
//            ((Blah2<Integer, Exception, Integer>) (Integer i) -> new Right(i + 1))
//            .create(((Integer i) -> new Right(i * 1)))
//            .create(((Integer i) -> new Right("_" + i + "_"))
//            .create()));


//        Function<Integer, Either<Exception, String>> compoundFunc =
//                new Blah((Integer i) -> new Right(i + 1)).create(
//                        new Blah((Integer i) -> new Right(i * 1)).create(
//                                new Blah((Integer i) -> new Right("_" + i + "_")))).apply(1);
    }

//    private final Function<S, Either<L, R>> func;

//    public Blah(Function<S, Either<L, R>> func) {
//        this.func = func;
//    }

    Either<L, R> apply(S s);

//    default <R2> Function<S, Either<L, R2>> create(Function<R, Either<L, R2>> nextBlah) {
//    default <R2> Blah<R, L, R2> create(Function<R, Either<L, R2>> nextBlah) {
//        return (S s) -> {
//            Either<L, R> result = apply(s);
//            if (result.isLeft()) {
//                return new Left(result.getLeft().get());
//            } else {
//                return nextBlah.apply(result.getRight().get());
//            }
//        };
//    }

    default Function<S, Either<L, R>> create() {
        return s -> {
            Either<L, R> result = apply(s);
            return result;
        };
    }

//    public <R2> Function<S, Either<L, R2>> create(Blah<R, L, R2> nextBlah) {
//        return s -> {
//            Either<L, R> result = func.apply(s);
//            if (result.isLeft()) {
//                return new Left(result.getLeft().get());
//            } else {
//                return nextBlah.func.apply(result.getRight().get());
//            }
//        };
//    }

}

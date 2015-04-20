package util.lambdaplus.lambda.experimental;

import util.lambdaplus.lambda.either.Either;
import util.lambdaplus.lambda.either.Left;

import java.util.function.Function;

/*
    Blah(
 */
public class Blah<S, L, R> {

    public static void main (String [] args) {
//        Blah<Integer, Exception, Integer> blah1 = new Blah((Integer i) -> new Right(i + 1));
//        Blah blah2 = new Blah((Integer i) -> new Right(i * 1));
//        Blah blah3 = new Blah((Integer i) -> new Right("_" + i + "_"));
//        Function<Integer, Either<Exception, String>> compoundFunc =
//                blah1.create(blah2.create(blah3.create()));

//        Function<Integer, Either<Exception, String>> compoundFunc =
//                new Blah((Integer i) -> new Right(i + 1)).create(
//                        new Blah((Integer i) -> new Right(i * 1)).create(
//                                new Blah((Integer i) -> new Right("_" + i + "_")))).apply(1);
    }

    private final Function<S, Either<L, R>> func;

//    public Blah(Function<S, Either<L, R>> func) {
//        this.func = func;
//    }

    public Blah(Function<S, Either<L, R>> func) {
        this.func = func;
    }

    public <R2> Function<S, Either<L, R2>> create(Function<R, Either<L, R2>> nextBlah) {
        return s -> {
            Either<L, R> result = func.apply(s);
            if (result.isLeft()) {
                return new Left(result.getLeft().get());
            } else {
                return nextBlah.apply(result.getRight().get());
            }
        };
    }

    public Function<S, Either<L, R>> create() {
        return s -> {
            Either<L, R> result = func.apply(s);
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

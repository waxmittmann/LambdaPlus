package util.lambdaplus.lambda.either;

import util.lambdaplus.lambda.util.ThrowableFunction;

public class LiftableFunction2<R1, R2> {
//    public <R3> LiftableFunction<R1, R2> andThen(LiftableFunction<R2, R3> next) {
//
//    }

    EitherFunction<R1, Exception, R2>  thisFunc;

    public LiftableFunction2(EitherFunction<R1, Exception, R2> thisFunc) {
        this.thisFunc = thisFunc;
    }

    public Either<Exception, R2> apply(R1 r1) {
        return thisFunc.apply(r1);
    }

    public <R3> LiftableFunction2<R1, R3> andThen(ThrowableFunction<R2, R3> next) {
        return new LiftableFunction2<>(
            (R1 r1) -> {
                try {
                    Either<Exception, R2> result = thisFunc.apply(r1);
                    if (result.isLeft()) {
                        return new Left<>(result.getLeft().get());
                    } else {
                        return new Right<>(next.apply(result.getRight().get()));
                    }
                } catch (Exception e) {
                    return new Left<>(e);
                }
            });
    }


//    static <V, R> LiftableFunction2<V, Exception, R> compose(LiftableFunction2<? super V, ? extends T> before) {
//        Objects.requireNonNull(before);
//        return (V v) -> {
//            Either<Exception, ? extends T> result = LambdaUtils.liftThrowable(before).apply(v);
//            if (result.isLeft()) {
//                return new Left<Exception, R>(result.getLeft().get());
//            } else {
//                return LambdaUtils.liftThrowable(this).apply(result.getRightValueOrException());
//            }
//        };
//    }

}

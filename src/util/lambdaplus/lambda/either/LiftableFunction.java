package util.lambdaplus.lambda.either;

import util.lambdaplus.lambda.util.ThrowableFunction;

public class LiftableFunction<R1, R2> {

    EitherFunction<R1, Exception, R2>  thisFunc;

    public LiftableFunction(ThrowableFunction<R1, R2> throwableFunc) {
        thisFunc = (R1 r1) -> {
            try {
                return new Right<>(throwableFunc.apply(r1));
            } catch (Exception e) {
                return new Left<>(e);
            }
        };
    }

    public LiftableFunction(EitherFunction<R1, Exception, R2> thisFunc) {
        this.thisFunc = thisFunc;
    }

    public Either<Exception, R2> apply(R1 r1) {
        return thisFunc.apply(r1);
    }

    public <R3> LiftableFunction<R1, R3> andThen(ThrowableFunction<R2, R3> next) {
        return new LiftableFunction<>(
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

    public <R3> LiftableFunction<R3, R2> compose(ThrowableFunction<R3, R1> next) {
        return new LiftableFunction<>(
            (R3 r3) -> {
                try {
                    Either<Exception, R1> result = new Right(next.apply(r3));
                    if (result.isLeft()) {
                        return new Left<>(result.getLeft().get());
                    } else {
                        return apply(result.getRight().get());
                    }
                } catch (Exception e) {
                    return new Left<>(e);
                }
            });
    }
}

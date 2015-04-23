package util.lambdaplus.lambda.either;

import util.lambdaplus.lambda.util.ThrowingFunction;

import static util.lambdaplus.lambda.util.LambdaUtils.liftThrowable;

public class LiftingFunction<R1, R2> implements EitherFunction<R1, Exception, R2>{

    EitherFunction<R1, Exception, R2>  thisFunc;

    public LiftingFunction(ThrowingFunction<R1, R2> throwableFunc) {
        thisFunc = liftThrowable(throwableFunc)::apply;
    }

    public LiftingFunction(EitherFunction<R1, Exception, R2> thisFunc) {
        this.thisFunc = thisFunc;
    }

    public Either<Exception, R2> apply(R1 r1) {
        return thisFunc.apply(r1);
    }

    public <R3> LiftingFunction<R1, R3> andThen(ThrowingFunction<R2, R3> next) {
        return new LiftingFunction<>(
            (R1 r1) -> {
                Either<Exception, R2> result = thisFunc.apply(r1);
                return result.fold(
                    (e) -> new Left<Exception, R3>(e), //Type arguments for Left are not redundant as IntelliJ suggests...
                    liftThrowable(next)::apply
                );
            });
    }

    public <R3> LiftingFunction<R3, R2> compose(ThrowingFunction<R3, R1> next) {
        return new LiftingFunction<>(
            (R3 r3) -> {
                Either<Exception, R1> result = liftThrowable(next).apply(r3);
                return result.fold(
                        e -> new Left<Exception, R2>(e), //Type arguments for Left are not redundant as IntelliJ suggests...
                        this::apply
                );
            });
    }
}

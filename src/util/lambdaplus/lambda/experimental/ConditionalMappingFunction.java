package util.lambdaplus.lambda.experimental;

import util.lambdaplus.lambda.either.Either;
import util.lambdaplus.lambda.either.Left;
import util.lambdaplus.lambda.either.Right;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static util.lambdaplus.lambda.util.lambda.LambdaUtils.either;
import static util.lambdaplus.lambda.util.lambda.LambdaUtils.eitherFunc;

public class ConditionalMappingFunction<IN, OUT, STATUS> {
    private final Function<IN, Either<STATUS, OUT>> mappingFunc;

    public ConditionalMappingFunction(Function<IN, Either<STATUS, OUT>> mappingFunc) {
        this.mappingFunc = mappingFunc;
    }

    public static <IN, OUT, STATUS> ConditionalMappingFunction<IN, OUT, STATUS> first(Function<IN, Either<STATUS, OUT>> mappingFunc) {
        return new ConditionalMappingFunction<>(mappingFunc);
    }

    public static <IN, STATUS> ConditionalMappingFunction<IN, IN, STATUS> first2(Function<IN, Optional<STATUS>> mappingFunc) {
        return new ConditionalMappingFunction<>((IN in) -> {
            Optional<STATUS> result = mappingFunc.apply(in);
            if (result.isPresent()) {
                return new Left<>(result.get());
            } else {
                return new Right<>(in);
            }
        });
    }

    public<NEXTOUT> ConditionalMappingFunction<IN, NEXTOUT, STATUS> andThen(Function<OUT, Either<STATUS, NEXTOUT>> nextMappingFunc) {
        return new ConditionalMappingFunction<IN, NEXTOUT, STATUS>((IN in) -> {
            Either<STATUS, OUT> result = mappingFunc.apply(in);
            return result.flatMap(
                (STATUS left) -> new Left<STATUS, NEXTOUT>(left),
                (OUT right) -> nextMappingFunc.apply(right)
            );
        });
    }

    public ConditionalMappingFunction<IN, OUT, STATUS> andThen2(Function<OUT, Optional<STATUS>> nextMappingFunc) {
        return new ConditionalMappingFunction<IN, OUT, STATUS>((IN in) -> {
            Either<STATUS, OUT> result = mappingFunc.apply(in);
            final Either<STATUS, OUT> statusObjectEither = result.flatMap(
                    (STATUS left) -> new Left<>(left),
                    (OUT right) -> {
                        Optional<STATUS> status = nextMappingFunc.apply(right);
                        if (status.isPresent()) {
                            return new Left<>(status.get());
                        } else {
                            return new Right<>(right);
                        }
                    }
            );
            return statusObjectEither;
        });
    }

    public static void main (String [] args) {
        {
            ConditionalMappingFunction<Integer, String, Exception> testFunction =
                    first2((Integer in) ->
                            in < 10 ? empty() : of(new Exception("Must be smaller than 10, was " + in)))
                    .andThen2((Integer in) ->
                        in % 2 == 1 ? empty() : of(new Exception("Must be odd, was " + in)))
                    .andThen((Integer in) -> {
                        if (in % 10 > 5) {
                            return new Right<>("Result was: " + in);
                        } else {
                            return new Left<>(new Exception("Last digit must be > 5, was " + in));
                        }
                    });

            for (int i = 0; i < 11; i++) {
                Either<Exception, String> result = testFunction.apply(i);
                System.out.print("For " + i + " got: ");
                result.consume(
                        l -> System.out.println("(Exception: " + l.getMessage() + ")"),
                        r -> System.out.println(r)
                );
            }
        }

        {
            ConditionalMappingFunction<Integer, String, Exception> testFunction =
                    first((Integer in) -> eitherFunc((Integer _in) -> _in % 2 == 1,
                            (_in) -> new Exception("Must be smaller than 10, was " + in), (_in) -> _in).apply(in))
                    .andThen((Integer in) -> eitherFunc((Integer _in) -> _in % 2 == 1,
                            (_in) -> new Exception("Must be odd, was " + _in), (_in) -> _in).apply(in))
                    .andThen((Integer in) -> eitherFunc((Integer _in) -> _in % 10 > 5,
                            (_in) -> new Exception("Last digit must be > 5, was " + in), (_in) -> "Result was: " + in).apply(in));

            ConditionalMappingFunction<Integer, String, Exception> testFunction2 =
                    first((Integer in) ->
                            either(in, (_in) -> _in < 10, (_in) -> new Exception("Must be smaller than 10, was " + in), (_in) -> _in))
                    .andThen((Integer in) ->
                            either(in, (_in) -> _in % 2 == 1, (_in) -> new Exception("Must be odd, was " + _in), (_in) -> _in))
                    .andThen((Integer in) ->
                            either(in, (Integer _in) -> _in % 10 > 5,
                                    (_in) -> new Exception("Last digit must be > 5, was " + in), (_in) -> "Result was: " + in));


            for (int i = 0; i < 11; i++) {
                Either<Exception, String> result = testFunction.apply(i);
                System.out.print("For " + i + " got: ");
                result.consume(
                        l -> System.out.println("(Exception: " + l.getMessage() + ")"),
                        r -> System.out.println(r)
                );
            }
        }
    }

    private Either<STATUS, OUT> apply(IN in) {
        return mappingFunc.apply(in);
    }
}

package util.lambdaplus.lambda.experimental;

/*
        CondTestFunction<S, T> condTestFunc =
            first()
            .andThen()
            .andThen()
            .andThen();

 */


import util.lambdaplus.lambda.either.Either;
import util.lambdaplus.lambda.either.Left;
import util.lambdaplus.lambda.either.Right;

import java.util.function.Function;

public class CondTestFunction<IN, OUT, STATUS> {
    private final Function<IN, Either<STATUS, OUT>> mappingFunc;

    public CondTestFunction(Function<IN, Either<STATUS, OUT>> mappingFunc) {
        this.mappingFunc = mappingFunc;
    }

    public static <IN, OUT, STATUS> CondTestFunction<IN, OUT, STATUS> first(Function<IN, Either<STATUS, OUT>> mappingFunc) {
        return new CondTestFunction<>(mappingFunc);
    }

    public<NEXTOUT> CondTestFunction<IN, NEXTOUT, STATUS> andThen(Function<OUT, Either<STATUS, NEXTOUT>> nextMappingFunc) {
        return new CondTestFunction<IN, NEXTOUT, STATUS>((IN in) -> {
            Either<STATUS, OUT> result = mappingFunc.apply(in);
            return result.flatMap(
                (STATUS left) -> new Left<STATUS, NEXTOUT>(left),
                (OUT right) -> nextMappingFunc.apply(right)
            );
        });
    }

    public static void main (String [] args) {
        CondTestFunction<Integer, String, Exception> testFunction =
            first((Integer in) -> {
                if (in < 10) {
                    return new Right<>(in);
                } else {
                    return new Left<>(new Exception("Must be smaller than 10, was " + in));
                }
            }).andThen((Integer in) -> {
                if (in % 2 == 1) {
                    return new Right<>(in);
                } else {
                    return new Left<>(new Exception("Must be odd, was " + in));
                }
            }).andThen((Integer in) -> {
                if (in % 10 > 5) {
                    return new Right<>("Result was: " + in);
                } else {
                    return new Left<>(new Exception("Last digit must be > 5, was " + in));
                }
            });

        for(int i = 0; i < 11; i++) {
            Either<Exception, String> result = testFunction.apply(i);
            System.out.print("For " + i + " got: ");
            result.consume(
                    l -> System.out.println("(Exception: " + l.getMessage() + ")"),
                    r -> System.out.println(r)
            );
        }


        //SomeFunc<In, Either<Out, Status>> someFunc;
        //public Either<Out, Status> SomeFunc(In in, Function<In, Either<Out, Status>> mapper)

//        {
//            OptionalFunction of = OptionalFunction.first((Integer in) -> {
//                if (in < 10) {
//                    return Optional.of(in);
//                } else {
//                    return Optional.empty();
//                }
//            }).andThen((Integer in) -> {
//                if (in % 2 == 1) {
//                    return Optional.of(in);
//                } else {
//                    return Optional.empty();
//                }
//            });
//
//            System.out.println(of.apply(8));
//            System.out.println(of.apply(9));
//            System.out.println(of.apply(11));
//        }

//        {
//            Function<Integer, Tuple2<Integer, Boolean>> f1
//                    = (Integer in) -> {
//                if (in < 10) {
//                    return new Tuple2<>(in, true);
//                } else {
//                    return new Tuple2<>(in, false);
//                }
//            };
//
//            Function<Tuple2<Integer, Boolean>, Tuple2<Integer, Boolean>> f2
//                    = (Tuple2<Integer, Boolean> in) -> {
//                if (in.getSecond() && in.getFirst() % 2 == 1) {
//                    return new Tuple2<>(in.getFirst(), true);
//                } else {
//                    return new Tuple2<>(in.getFirst(), false);
//                }
//            };
//
//            System.out.println(f1.andThen(f2).apply(9).getSecond());
//            System.out.println(f1.andThen(f2).apply(10).getSecond());
//            System.out.println(f1.andThen(f2).apply(8).getSecond());
//        }
    }

    private Either<STATUS, OUT> apply(IN in) {
        return mappingFunc.apply(in);
    }
}

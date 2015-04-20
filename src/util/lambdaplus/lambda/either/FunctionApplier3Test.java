package util.lambdaplus.lambda.either;

import java.util.function.Function;

public class FunctionApplier3Test {
    public static void main (String [] args) {
        {
            EitherFunction<Integer, Integer, Exception> f1 = (i) -> new Right(i + 1);
            EitherFunction<Integer, String, Exception> f2 = (i) -> new Right(String.valueOf(i * 2));
            EitherFunction<String, String, Exception> f3 = (i) -> new Right(i + "_");

            Either<Exception, String> result =
                f1.andThen(
                f2.andThen(
                f3))
                .apply(1);
            System.out.println(result.getRight().get());
        }

        {
            Either<Exception, String> result =
                ((EitherFunction<Integer, Integer, Exception>) (i) -> new Right(i + 1)).andThen(
                ((EitherFunction<Integer, String, Exception>) (i) -> new Right(String.valueOf(i * 2))).andThen(
                (EitherFunction<String, String, Exception>) (i) -> new Right(i + "_")))
                .apply(1);
            System.out.println(result.getRight().get());
        }

        {
            Either<Exception, String> result =
                EitherFunction.create((Integer i) -> new Right(i + 1)).andThen(
                EitherFunction.create(((Integer i) -> new Right(String.valueOf(i * 2)))).andThen(
                EitherFunction.create(((String i) -> new Right(i + "_")))))
                .apply(1);
            System.out.println(result.getRight().get());
        }

        {
            EitherFunction<Integer, Integer, Exception> f1 = (i) -> new Right(i + 1);
            EitherFunction<Integer, String, Exception> f2 = (i) -> new Right(String.valueOf(i * 2));
            EitherFunction<String, String, Exception> f3 = (i) -> new Right(i + "_");

            Either<Exception, String> result = EitherFunction.doInTurn(1, f1, f2, f3);

            result.consume(
                    System.out::println,
                    System.out::println
            );
        }

        {

            Either<Exception, String> result = EitherFunction.doInTurn(1,
                    (Integer i) -> new Right(i + 1),
                    (Integer i) -> new Right(String.valueOf(i * 2)),
                    (String i) -> new Right(i + "_"));

            result.consume(
                    System.out::println,
                    System.out::println
            );
        }

//        if (false) {
//            Either<Exception, String> result = FunctionApplier3.<Exception, String, Integer>createDoInTurn(
//                    (Integer i) -> new Right(i + 1),
//                    (Integer i) -> new Right(String.valueOf(i * 2)),
//                    (String i) -> new Right(i + "_")).apply(1);
//
//            result.consume(
//                    System.out::println,
//                    System.out::println
//            );
//        }

        {
            Function<Integer, Either<Exception, String>> doInTurn =
                    EitherFunction.<Exception, String, Integer>createDoInTurn(
                            (Integer i) -> new Right(i + 1),
                            (Integer i) -> new Right(String.valueOf(i * 2)),
                            (String i) -> new Right(i + "_"));
            Either<Exception, String> result = doInTurn.apply(1);

            result.consume(
                    System.out::println,
                    System.out::println
            );
        }
    }
}

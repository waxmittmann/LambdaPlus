package util.lambdaplus.lambda.either;

import java.util.function.Function;

public class FunctionApplier3Test {
    public static void main (String [] args) {
        {
            FunctionApplier3<Integer, Integer, Exception> f1 = (i) -> new Right(i + 1);
            FunctionApplier3<Integer, String, Exception> f2 = (i) -> new Right(String.valueOf(i * 2));
            FunctionApplier3<String, String, Exception> f3 = (i) -> new Right(i + "_");

            Either<Exception, String> result =
                f1.andThen(
                f2.andThen(
                f3))
                .apply(1);
            System.out.println(result.getRight().get());
        }

        {
            Either<Exception, String> result =
                ((FunctionApplier3<Integer, Integer, Exception>) (i) -> new Right(i + 1)).andThen(
                ((FunctionApplier3<Integer, String, Exception>) (i) -> new Right(String.valueOf(i * 2))).andThen(
                        (FunctionApplier3<String, String, Exception>) (i) -> new Right(i + "_")))
                .apply(1);
            System.out.println(result.getRight().get());
        }

        {
            FunctionApplier3<Integer, Integer, Exception> f1 = (i) -> new Right(i + 1);
            FunctionApplier3<Integer, String, Exception> f2 = (i) -> new Right(String.valueOf(i * 2));
            FunctionApplier3<String, String, Exception> f3 = (i) -> new Right(i + "_");

            Either<Exception, String> result = FunctionApplier3.doInTurn(1, f1, f2, f3);

            result.consume(
                    System.out::println,
                    System.out::println
            );
        }

        {

            Either<Exception, String> result = FunctionApplier3.doInTurn(1,
                (Integer i) -> new Right(i + 1),
                (Integer i) -> new Right(String.valueOf(i * 2)),
                (String i) -> new Right(i + "_"));

            result.consume(
                    System.out::println,
                    System.out::println
            );
        }

        {
            Either<Exception, String> result = FunctionApplier3.<Exception, String, Integer>createDoInTurn(
                    (Integer i) -> new Right(i + 1),
                    (Integer i) -> new Right(String.valueOf(i * 2)),
                    (String i) -> new Right(i + "_")).apply(1);

            result.consume(
                    System.out::println,
                    System.out::println
            );
        }

        {
            Function<Integer, Either<Exception, String>> doInTurn =
                    FunctionApplier3.<Exception, String, Integer>createDoInTurn(
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

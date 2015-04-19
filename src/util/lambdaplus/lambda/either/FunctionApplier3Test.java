package util.lambdaplus.lambda.either;

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
                ((Integer i) -> new Right(i + 1)).andThen(
                (Integer i) -> new Right(String.valueOf(i * 2))).andThen(
                (FunctionApplier3<String, String, Exception>) (i) -> new Right(i + "_")))
                .apply(1);
            System.out.println(result.getRight().get());
        }

    }
}

package util.lambdaplus.lambda.experimental.ifelseif;

import util.lambdaplus.functionalif.If;
import util.lambdaplus.functionalif.IfFactory;

import java.util.function.BiFunction;

public class IfElseIfTest {
    private static boolean DEBUG = false;

    public static void main (String [] args) {

//        If<Integer> cond =
//            If.<Integer>
//                _if((in) -> in > 5, (in) -> in * 2)
//                ._elseIf((in) -> in < 2, (in) -> in + 101)
//                    ._inIf(If.<Integer>
//                        _if((in) -> in % 2 == 0, (in) -> in * 2)
//                        ._else((in) -> in * 100)
//                    )
//                ._else((in) -> -999);

        BiFunction<String, Integer, Integer> pr = (String s, Integer i) -> {
//            System.out.println(s + ": " + i);
            return i;
        };

//        If<Integer> cond =
//            If.<Integer>
//                _if((in) -> in > 5, (in) -> pr.apply("Inside if_1", 1))
//                ._elseIf((in) -> in < 2, (in) -> pr.apply("Inside if_2", in))
//                    ._inIf(If.<Integer>
//                        _if((in) -> in % 2 == 0, (in) -> pr.apply("Inside if_2_1", 2))
//                        ._else((in) -> pr.apply("Inside else_2_2", 3))
//                    )
//                ._else((in) -> pr.apply("Inside if_3", 4));

        /*
                int in = ?;
                if (in > 5) {
                    in = 1;
                } else if (in < 2) {
                    if (in % 2) {
                        in = 2;
                    } else {
                        in = 3;
                    }
                } else {
                    in = 4;
                }
         */
        /*
            _if((in) -> in > 5, (in) -> 1)
            ._elseIf((in) -> in < 2,
                _if((in) -> in % 2 == 0, (in) -> 2)
                ._else((in) -> 3))
            ._else((in) -> 4);
         */
        IfFactory<Integer> ifF = new IfFactory<>();

        If<Integer> cond1 =
            ifF._if((in) -> in > 5, (in) -> pr.apply("Inside if_1", 1))
            ._elseIf((in) -> in < 2, (in) -> pr.apply("Inside if_2", in),
                    ifF._if((in) -> in % 2 == 0, (in) -> pr.apply("Inside if_2_1", 2))
                            ._else((in) -> pr.apply("Inside else_2_2", 3))
            )
            ._else((in) -> pr.apply("Inside if_3", 4));

        If<Integer> cond2 =
            ifF.
            _if((in) -> in > 5, (in) -> pr.apply("Inside if_1", 1))
            ._elseIf(ifF._if((in) -> in < 2, (in) -> pr.apply("Inside if_2", in))._inside(ifF.
                            _if((in) -> in % 2 == 0, (in) -> pr.apply("Inside if_2_1", 2))
                            ._else((in) -> pr.apply("Inside else_2_2", 3))
            ))
            ._else((in) -> pr.apply("Inside if_3", 4));


        for(int i = 0; i < 10; i++) {
//            System.out.println("For " + i + " ---");
//            System.out.print(cond1.applyExtract(i) + ", ");
            final Integer integer = cond1.applyExtract(i);
            final Integer integer1 = cond2.applyExtract(i);
            if (integer != integer1) {
                System.err.print(integer + " != " + integer1);
            }
            System.out.print("(" + integer + ", ");
            System.out.print(integer1 + "), ");
        }
        System.out.println();
    }

}

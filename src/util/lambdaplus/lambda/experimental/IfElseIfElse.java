package util.lambdaplus.lambda.experimental;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class IfElseIfElse {
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

        If<Integer> cond =
                If.<Integer>
                        _if((in) -> in > 5, (in) -> pr.apply("Inside if_1", 1))
                        ._elseIf((in) -> in < 2, (in) -> pr.apply("Inside if_2", in),
                                If.<Integer>_if((in) -> in % 2 == 0, (in) -> pr.apply("Inside if_2_1", 2))
                                        ._else((in) -> pr.apply("Inside else_2_2", 3))
                        )
                        ._else((in) -> pr.apply("Inside if_3", 4));

        for(int i = 0; i < 10; i++) {
//            System.out.println("For " + i + " ---");
            System.out.print(cond.applyExtract(i) + ", ");
        }
        System.out.println();
    }

    public static class If<S> {
        private final Function<S, Optional<S>> ifMapper;

        public If(Function<S, Boolean> conditional, Function<S, S> stateModifier) {
            ifMapper = (S s) -> {
                if (DEBUG) System.out.println("In if, state is: " + s);
                if (conditional.apply(s)) {
                    return Optional.of(stateModifier.apply(s));
                }
                return Optional.empty();
            };
        }

        public If(Function<S, Optional<S>> ifMapper) {
            this.ifMapper = ifMapper;
        }

        public static<S> If<S> _if(Function<S, Boolean> conditional, Function<S, S> stateModifier) {
            return new If<>(conditional, stateModifier);
        }

        public static<S> If<S> _if(Function<S, Boolean> conditional, Function<S, S> stateModifier, If<S> ... insideIfs) {
            If<S> thisIf = new If<>(conditional, stateModifier);
            return new If<>((S s) -> {
                Optional<S> newState = thisIf.apply(s);
                if (newState.isPresent()) {
                    for(If<S> insideIf : insideIfs) {
                        Optional<S> nextState = insideIf.apply(newState.get());
                        if (nextState.isPresent()) {
                            newState = nextState;
                        }
                    }
                }
                return newState;
            });
        }


        public Optional<S> apply(S s) {
            return ifMapper.apply(s);
        }

        public S applyExtract(S s) {
            return ifMapper.apply(s).get();
        }

//        public If<S> _inIf(Function<S, Boolean> conditional, Function<S, S> stateModifier) {
//            If<S> nextIf = new If<>(conditional, stateModifier);
//            return _inIf(nextIf);
//        }
//
//        public If<S> _inIf(If<S> nextIf) {
//            return new If<>(
//                    (S s) -> {
//                        if (DEBUG) System.out.println("In inIf, state is: " + s);
//                        Optional<S> newState = apply(s);
//                        if (newState.isPresent()) {
//                            final Optional<S> nextState = nextIf.apply(newState.get());
//                            if (nextState.isPresent()) {
//                                newState = nextState;
//                            }
//                        }
//                        return newState;
//                    }
//            );
//        }

//        , If<S> ... insideIfs) {

        public If<S> _elseIf(Function<S, Boolean> conditional, Function<S, S> stateModifier, If<S> ... insideIfs) {
            If<S> nextIf = new If<>(conditional, stateModifier);
            return _elseIf(nextIf, insideIfs);
        }

        public If<S> _elseIf(If<S> nextIf, If<S> ... insideIfs) {
            return new If<S>(
                    (S s) -> {
                        if (DEBUG) System.out.println("In _elseIf, state is: " + s);
                        Optional<S> nextState = apply(s);
                        if (!nextState.isPresent()) {
                            nextState = nextIf.apply(s);
                            if (nextState.isPresent()) {
                                for (If<S> insideIf : insideIfs) {
                                    Optional<S> newState = insideIf.apply(nextState.get());
                                    if (newState.isPresent()) {
                                        nextState = newState;
                                    }
                                }
                            }
                            return nextState;
                        } else {
                            return nextState;
                        }
                    }
            );
        }

        public If<S> _else(Function<S, S> stateModifier) {
            return new If<>(
                (S s) -> {
                    if (DEBUG) System.out.println("In _else, state is: " + s);
                    Optional<S> newState = apply(s);
                    if (!newState.isPresent()) {
                        return Optional.of(stateModifier.apply(s));
                    } else {
                        return newState;
                    }
                }
            );
        }
    }

    public static class Else<S> extends If<S> {
        public Else(Function<S, S> stateModifier) {
            super((S __) -> true, stateModifier);
        }
    }
}

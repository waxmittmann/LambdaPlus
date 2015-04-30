package util.lambdaplus.lambda.experimental.ifelseif;

import java.util.Optional;
import java.util.function.Function;

public class If<S> {
    private final Function<S, Optional<S>> ifMapper;

    public If(Function<S, Boolean> conditional, Function<S, S> stateModifier) {
        ifMapper = (S s) -> {
            if (conditional.apply(s)) {
                return Optional.of(stateModifier.apply(s));
            }
            return Optional.empty();
        };
    }

    public If(Function<S, Optional<S>> ifMapper) {
        this.ifMapper = ifMapper;
    }

    public If<S> _inside(If<S> insideIf) {
        return new If<>((S s) ->{
            Optional<S> nextState = apply(s);
            if (nextState.isPresent()) {
                Optional<S> newState = insideIf.apply(nextState.get());
                if (newState.isPresent()) {
                    nextState = newState;
                }
            }
            return nextState;
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

    public If<S> _elseIf(Function<S, Boolean> conditional, Function<S, S> stateModifier, If<S>... insideIfs) {
        If<S> nextIf = new If<>(conditional, stateModifier);
        return _elseIf(nextIf, insideIfs);
    }

    public If<S> _elseIf(If<S> nextIf, If<S>... insideIfs) {
        return new If<S>(
                (S s) -> {
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

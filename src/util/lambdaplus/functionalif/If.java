package util.lambdaplus.functionalif;

import java.util.Optional;
import java.util.function.Function;

public class If<S> {
//    public static class Else<S> extends If<S> {
//        public Else(Function<S, S> stateModifier) {
//            super((__) -> true, stateModifier);
//        }
//    }

    private final Function<S, Optional<S>> ifMapper;

    public If(Function<S, Optional<S>> ifMapper) {
        this.ifMapper = ifMapper;
    }

    public If(Function<S, Boolean> conditional, Function<S, S> stateModifier) {
        this((S s) -> {
            if (conditional.apply(s)) {
                return Optional.of(stateModifier.apply(s));
            }
            return Optional.empty();
        });
    }

    public If<S> _elseIf(Function<S, Boolean> conditional, Function<S, S> stateModifier, If<S>... insideIfs) {
        If<S> nextIf = new If<>(conditional, stateModifier);
        return _elseIf(nextIf, insideIfs);
    }

    //Todo: Simplify this ... do we even want insideIfs? Or is _inside good enough?
    public If<S> _elseIf(If<S> nextIf, If<S>... insideIfs) {
        return new If<>(
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

    public Optional<S> apply(S s) {
        return ifMapper.apply(s);
    }

    public S applyExtract(S s) {
        return ifMapper.apply(s).get();
    }
}

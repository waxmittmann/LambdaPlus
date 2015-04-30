package util.lambdaplus.lambda.experimental.ifelseif;

import java.util.Optional;
import java.util.function.Function;

public class IfFactory<S> {
    public If<S> _if(Function<S, Boolean> conditional, Function<S, S> stateModifier) {
        return new If<>(conditional, stateModifier);
    }

    public If<S> _if(Function<S, Boolean> conditional, Function<S, S> stateModifier, If<S>... insideIfs) {
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
}

package util.lambdaplus.lambda.experimental.ifelseif;

import java.util.function.Function;

public class Else<S> extends If<S> {
    public Else(Function<S, S> stateModifier) {
        super((S __) -> true, stateModifier);
    }
}

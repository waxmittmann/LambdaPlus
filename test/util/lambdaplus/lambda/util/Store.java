package util.lambdaplus.lambda.util;

public class Store <S> {
    S s;

    public Store() {
        s = null;
    }

    public Store(S s) {
        this.s = s;
    }

    public S get() {
        return s;
    }

    public void set(S s) {
        this.s = s;
    }
}

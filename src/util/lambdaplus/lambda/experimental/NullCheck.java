package util.lambdaplus.lambda.experimental;

import java.util.function.Function;

public class NullCheck<S, T> {
    private final Function<S, T> nullCheckMapper;

    public static class A {
        B b;

        public B getB() {
            return b;
        }

        public A(B b) {

            this.b = b;
        }
    }

    public static class B {
        C c;

        public C getC() {
            return c;
        }

        public B(C c) {

            this.c = c;
        }
    }

    public static class C {

    }

    public static class D {
        public D getD() {
            return d;
        }

        private final D d;

        public D(D d) {
            this.d = d;
        }

        public D() {
            this.d = null;
        }
    }

    public static void main (String [] args) {
        A a1 = new A(new B(new C()));
        A a2 = new A(new B(null));
        A a3 = new A(null);

        D d0 = null;
        D d1 = new D(new D(new D(new D(new D(new D())))));


        NullCheck<A, C> nullCheck =
                first((A in) -> (in.getB()))
                .andThen((B in) -> in.getC());

        System.out.println(nullCheck.isNull(a1));
        System.out.println(nullCheck.isNull(a2));
        System.out.println(nullCheck.isNull(a3));

        final Function<D, D> d_to_d = (D in) -> in.getD();
        NullCheck<D, D> nullCheck2_5 = new NullCheck<>(d_to_d)
                .andThen(d_to_d)
                .andThen(d_to_d)
                .andThen(d_to_d)
                .andThen(d_to_d);

        NullCheck<D, D> nullCheck2_6 = new NullCheck<>(d_to_d)
                .andThen(d_to_d)
                .andThen(d_to_d)
                .andThen(d_to_d)
                .andThen(d_to_d)
                .andThen(d_to_d);

        NullCheck<D, D> nullCheck2_7 = new NullCheck<>(d_to_d)
                .andThen(d_to_d)
                .andThen(d_to_d)
                .andThen(d_to_d)
                .andThen(d_to_d)
                .andThen(d_to_d)
                .andThen(d_to_d);

        System.out.println(nullCheck2_5.isNull(d0));
        System.out.println(nullCheck2_5.isNull(d1));
        System.out.println(nullCheck2_6.isNull(d1));
        System.out.println(nullCheck2_7.isNull(d1));


    }

    public NullCheck(Function<S, T> nullCheckMapper) {
        this.nullCheckMapper = nullCheckMapper;
    }

    public static <S, T> NullCheck<S, T> first(Function<S, T> nullCheckMapper) {
        return new NullCheck<S, T>(nullCheckMapper);
    }

    public <U> NullCheck<S, U> andThen(Function<T, U> nextMapper) {
        return new NullCheck<S, U>((S s) -> {
            if (s == null) {
                return null;
            }
            T t = nullCheckMapper.apply(s);
            if (t == null) {
                return null;
            }
            return nextMapper.apply(t);
        });
    }

    public boolean isNull(S s) {
        return nullCheckMapper.apply(s) == null;
    }

}

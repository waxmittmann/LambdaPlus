package lambda.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {
    public static <S> List<S> list(S ... values) {
        List<S> li = new ArrayList<>();

        for(S value : values) {
            li.add(value);
        }

        return li;
    }
}

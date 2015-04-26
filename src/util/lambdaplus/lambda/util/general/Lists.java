package util.lambdaplus.lambda.util.general;

import java.util.ArrayList;

public class Lists {
    static public<S> ArrayList<S> newArrayList(S ... sArray) {
        ArrayList<S> list = new ArrayList<>();
        for(S s : sArray) {
            list.add(s);
        }
        return list;
    }
}

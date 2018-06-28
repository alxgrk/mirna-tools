package de.alxgrk.filter;

import de.alxgrk.util.Pair;
import lombok.Value;

@Value
public class PairWithDistance {

    Pair<String> pair;

    Integer distance;

    public String toString() {
        return pair.toString();
    }

}

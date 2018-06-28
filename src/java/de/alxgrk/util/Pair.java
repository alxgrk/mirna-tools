package de.alxgrk.util;

import lombok.Value;

@Value
public class Pair<T> {

    T first;

    T second;

    public String toString() {
        return first + " <-> " + second;
    }

}

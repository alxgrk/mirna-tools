package de.alxgrk.filter;

import de.alxgrk.util.Pair;
import lombok.Value;

@Value
public class GenesAndMiRnas {

    Pair<String> genes;

    PairWithDistance miRnas;

}

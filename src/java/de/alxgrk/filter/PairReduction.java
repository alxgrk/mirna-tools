package de.alxgrk.filter;

import java.util.HashMap;
import java.util.Map;

import de.alxgrk.util.Pair;
import lombok.Value;

@Value
public class PairReduction {

    StringBuilder initial = new StringBuilder();

    Map<String, Integer> geneCount = new HashMap<>();

    Map<String, Integer> miRnaCount = new HashMap<>();

    Map<Integer, Integer> distanceCount = new HashMap<>();

    Map<Pair<String>, Integer> pairCount = new HashMap<>();

    public StringBuilder toStringWithSideEffects(StringBuilder former, GenesAndMiRnas pairs) {
        // count genes
        String gene = pairs.getGenes().getFirst();
        geneCount.merge(gene, 1, (o, n) -> o + n);

        // count miRNAs
        String firstMiRna = pairs.getMiRnas().getPair().getFirst();
        String secondMiRna = pairs.getMiRnas().getPair().getSecond();
        miRnaCount.merge(firstMiRna, 1, (o, n) -> o + n);
        miRnaCount.merge(secondMiRna, 1, (o, n) -> o + n);

        // count distances
        distanceCount.merge(pairs.getMiRnas().getDistance(), 1,
                (o, n) -> o + n);

        // count miRNA pairs while eliminating duplicates
        pairCount.merge(pairs.getMiRnas().getPair(), 1, (o, n) -> o + n);

        // build pretty result string
        StringBuilder result = new StringBuilder(gene)
                .append(" -> ")
                .append(pairs.getMiRnas());
        return former.append(result).append("\n");
    }

    public StringBuilder identity(StringBuilder oldSb, StringBuilder newSb) {
        return oldSb;
    }
}

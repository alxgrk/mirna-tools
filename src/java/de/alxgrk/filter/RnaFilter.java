package de.alxgrk.filter;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.alxgrk.Main;
import de.alxgrk.util.Functions;
import de.alxgrk.util.Output;
import de.alxgrk.util.Pair;
import de.alxgrk.util.Sort;

public class RnaFilter {

    public void filter(BufferedReader reader) throws IOException {
        PairReduction pairReduction = new PairReduction();

        StringBuilder reduced = reader.lines()
                .map(Functions::splitByTab)
                .filter(Functions::distanceLessThan150)
                .map(Functions::extractGeneRnaPairs)
                .map(Functions::groupByGenesAndMiRnas)
                .filter(Functions::onSameGene)
                .reduce(pairReduction.getInitial(),
                        pairReduction::toStringWithSideEffects,
                        pairReduction::identity);

        System.out.println(reduced);

        // count lines
        int numPairs = reduced.toString()
                .split(System.getProperty("line.separator")).length;
        System.out.println(numPairs);

        // sanity checks
        Integer genesSummedUp = pairReduction.getGeneCount().values().stream().reduce(0, (v1,
                v2) -> v1 + v2);
        if (numPairs != genesSummedUp)
            throw new IllegalStateException();

        Integer miRnasSummedUp = pairReduction.getMiRnaCount().values().stream().reduce(0, (v1,
                v2) -> v1 + v2);
        if (numPairs * 2 != miRnasSummedUp)
            throw new IllegalStateException();

        // print distribution
        Map<String, Integer> sortedGeneCount = Sort.byValue(pairReduction.getGeneCount());
        Map<String, Integer> sortedMiRnaCount = Sort.byValue(pairReduction.getMiRnaCount());

        // distances divided by 2
        Map<Integer, Integer> valuesDivided = new TreeMap<Integer, Integer>();

        for (Entry<Integer, Integer> entry : pairReduction.getDistanceCount().entrySet()) {
            Integer value = entry.getValue() / 2;
            valuesDivided.put(entry.getKey(), value);
        }

        // pairs containing TARGET_RNA
        Map<Pair<String>, Integer> pairCount = pairReduction.getPairCount()
                .entrySet()
                .stream()
                .filter(e -> e.getKey().getFirst().startsWith(Main.TARGET_RNA))
                .reduce(new HashMap<Pair<String>, Integer>(),
                        (m, e) -> {
                            m.put(e.getKey(), e.getValue());
                            return m;
                        },
                        (m1, m2) -> m1);

        FileWriter geneWriter = new FileWriter("gene-count.csv");
        FileWriter miRnaWriter = new FileWriter("mirna-count.csv");
        FileWriter distanceWriter = new FileWriter("distance-count.csv");
        FileWriter pairWriter = new FileWriter("pair-count.csv");

        Output.asCsv(sortedGeneCount, geneWriter);
        Output.asCsv(sortedMiRnaCount, miRnaWriter);
        Output.asCsv(valuesDivided, distanceWriter);
        Output.asCsv(Sort.byValue(pairCount), pairWriter);
    }

}

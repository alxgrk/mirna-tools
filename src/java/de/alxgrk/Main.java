package de.alxgrk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

    public static void main(String[] args) throws IOException {

        File input = new File(args[0]);

        try (BufferedReader reader = new BufferedReader(new FileReader(input));) {

            Map<String, Integer> geneCount = new HashMap<>();
            Map<String, Integer> miRnaCount = new HashMap<>();

            StringBuilder reduced = reader.lines()
                    // split by tab
                    .map(l -> l.split("\t"))
                    // only use distance <= 150
                    .filter(l -> Integer.parseInt(l[l.length - 1]) <= 150)
                    // extract gene-miRna pairs
                    .map(l -> {
                        String[] geneRnaPair = Arrays.stream(l)
                                .filter(c -> c.contains(":"))
                                .limit(2)
                                .toArray(String[]::new);
                        return new Pair<>(geneRnaPair[0], geneRnaPair[1]);
                    })
                    // group by gene / miRna
                    .map(p -> {
                        String[] first = p.getFirst().split(":", 2);
                        String firstGene = first[0];
                        String firstRna = first[1];

                        String[] second = p.getSecond().split(":", 2);
                        String secondGene = second[0];
                        String secondRna = second[1];

                        return new GenesAndMiRnas(
                                new Pair<>(firstGene, secondGene),
                                new Pair<>(firstRna, secondRna));
                    })
                    // only allow pairs, that are on the same gene
                    .filter(p -> p.getGenes().getFirst().equals(p.getGenes().getSecond()))
                    // sum up values
                    .reduce(new StringBuilder(),
                            (sb, p) -> {
                                // count genes
                                String gene = p.getGenes().getFirst();
                                geneCount.merge(gene, 1, (o, n) -> o + n);

                                // count miRNAs
                                String firstMiRna = p.getMiRnas().getFirst();
                                String secondMiRna = p.getMiRnas().getSecond();
                                miRnaCount.merge(firstMiRna, 1, (o, n) -> o + n);
                                miRnaCount.merge(secondMiRna, 1, (o, n) -> o + n);

                                // build pretty result string
                                StringBuilder result = new StringBuilder(gene)
                                        .append(" -> ")
                                        .append(firstMiRna)
                                        .append(" <-> ")
                                        .append(secondMiRna);
                                return sb.append(result).append("\n");
                            },
                            (s1, s2) -> s1.append(s2));

            System.out.println(reduced);

            // count lines
            int numPairs = reduced.toString()
                    .split(System.getProperty("line.separator")).length;
            System.out.println(numPairs);

            // sanity checks
            Integer genesSummedUp = geneCount.values().stream().reduce(0, (v1, v2) -> v1 + v2);
            if (numPairs != genesSummedUp)
                throw new IllegalStateException();

            Integer miRnasSummedUp = miRnaCount.values().stream().reduce(0, (v1, v2) -> v1 + v2);
            if (numPairs * 2 != miRnasSummedUp)
                throw new IllegalStateException();

            // print distribution
            System.out.println(sortByValue(geneCount));
            System.out.println(sortByValue(miRnaCount));
        }
    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> map) {
        List<Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue((i1, i2) -> i2.compareTo(i1)));

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

}
package de.alxgrk.util;

import java.util.Arrays;

import de.alxgrk.Main;
import de.alxgrk.filter.GenesAndMiRnas;
import de.alxgrk.filter.PairWithDistance;

public class Functions {

    public static String[] splitByTab(String line) {
        return line.split("\t");
    }

    public static boolean distanceLessThan150(String[] splitLine) {
        // the last element of the split line should be the distance
        return Integer.parseInt(splitLine[splitLine.length - 1]) <= 150;
    }

    public static boolean containsTargetRna(String[] splitLine) {
        return splitLine[3].contains(Main.TARGET_RNA) || splitLine[9].contains(Main.TARGET_RNA);
    }

    public static boolean upstreamOnly(String[] splitLine) {
        return Integer.parseInt(splitLine[1]) < Integer.parseInt(splitLine[7]);
    }

    public static PairWithDistance extractGeneRnaPairs(String[] splitLine) {
        String[] geneRnaPair = Arrays.stream(splitLine)
                .filter(c -> c.contains(":"))
                .limit(2)
                .toArray(String[]::new);
        Pair<String> pair = new Pair<>(geneRnaPair[0], geneRnaPair[1]);
        return new PairWithDistance(pair, Integer.parseInt(splitLine[splitLine.length - 1]));
    }

    public static GenesAndMiRnas groupByGenesAndMiRnas(PairWithDistance pair) {
        String[] first = pair.getPair().getFirst().split(":", 2);
        String firstGene = first[0];
        String firstRna = first[1];

        String[] second = pair.getPair().getSecond().split(":", 2);
        String secondGene = second[0];
        String secondRna = second[1];

        return new GenesAndMiRnas(
                new Pair<>(firstGene, secondGene),
                new PairWithDistance(new Pair<>(firstRna, secondRna), pair.getDistance()));
    }

    public static boolean onSameGene(GenesAndMiRnas p) {
        return p.getGenes().getFirst().equals(p.getGenes().getSecond());
    }

}

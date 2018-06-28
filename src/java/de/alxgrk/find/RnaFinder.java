package de.alxgrk.find;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import de.alxgrk.Main;
import de.alxgrk.util.Functions;
import lombok.SneakyThrows;

public class RnaFinder {

    public void findSequence(BufferedReader reader) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(Main.TARGET_RNA + "-seq.bed"));

        reader.lines()
                .map(Functions::splitByTab)
                .filter(Functions::distanceLessThan150)
                .filter(Functions::containsTargetRna)
                .filter(Functions::upstreamOnly)
                .map(this::sequenceLength200)
                .forEach(s -> throwingWrite(s, writer));

        writer.flush();
        writer.close();
    }

    private String sequenceLength200(String[] splitLine) {
        Integer startOfFirst = Integer.parseInt(splitLine[1]);
        Integer endOfSecond = Integer.parseInt(splitLine[8]);

        // determine sequence of length 200 to contain target site
        Integer distance = endOfSecond - startOfFirst;
        int oppositeDistance = 200 - distance;

        int ceiledHalf = (int) Math.ceil(oppositeDistance / 2);
        Integer sequenceStart = startOfFirst - ceiledHalf;
        Integer sequenceEnd = endOfSecond + ceiledHalf;

        int rest = oppositeDistance % 2;
        sequenceEnd += rest;

        return new StringBuilder()
                .append(splitLine[0])
                .append("\t")
                .append(sequenceStart)
                .append("\t")
                .append(sequenceEnd)
                .append("\t")
                .append(splitLine[3])
                .append("_")
                .append(startOfFirst)
                .append("_")
                .append(splitLine[2])
                .append("_")
                .append(splitLine[9])
                .append("_")
                .append(splitLine[7])
                .append("_")
                .append(endOfSecond)
                .append("\t")
                .append(splitLine[4])
                .append("\t")
                .append(splitLine[5])
                .append("\n")
                .toString();
    }

    @SneakyThrows
    private void throwingWrite(String toBeWritten, Writer writer) {
        writer.write(toBeWritten);
    }

}

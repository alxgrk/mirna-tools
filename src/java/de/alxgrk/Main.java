package de.alxgrk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.alxgrk.filter.RnaFilter;
import de.alxgrk.find.RnaFinder;

public class Main {

    public static final String TARGET_RNA = "miR-137";

    public static void main(String[] args) throws IOException {

        File input = new File(args[1]);

        try (BufferedReader reader = new BufferedReader(new FileReader(input));) {

            String command = args[0];

            if (command.equals("filter")) {
                RnaFilter rnaFilter = new RnaFilter();
                rnaFilter.filter(reader);
            }
            if (command.equals("find-seq")) {
                RnaFinder rnaFinder = new RnaFinder();
                rnaFinder.findSequence(reader);
            }

        }
    }

}
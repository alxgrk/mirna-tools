package de.alxgrk.util;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class Output {

    public static <K, V> void asCsv(Map<K, V> map, Writer output) throws IOException {
        try (ICsvListWriter listWriter = new CsvListWriter(output,
                CsvPreference.STANDARD_PREFERENCE)) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                listWriter.write(entry.getKey(), entry.getValue());
            }
        }
    }

}

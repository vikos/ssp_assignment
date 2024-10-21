package org.vikos;

import org.apache.commons.cli.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.vikos.data.processor.CSVToSKUMapper;
import org.vikos.data.processor.CSVToSKUMapper.*;
import org.vikos.data.processor.LanguageDetector;

import java.io.FileReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

import static org.vikos.data.processor.CSVToSKUMapper.*;

//TIP To <b>Run</b> "code",  press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static final String CSV_OPTION = "d";

    public static void main(String[] args) throws Exception {

        Options options = new Options();
        options.addOption(CSV_OPTION,  "data", true,  "CSV Data file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options,  args);

        if (!cmd.hasOption(CSV_OPTION)) {
            System.out.println("Missing Data option. Usage: <cmd> -d path/to/data.csv ");
        }

        String csvPath = cmd.getOptionValue(CSV_OPTION);

        if (csvPath == null || !Files.exists(Path.of(csvPath))){
            System.out.printf("Invalid CSV file path: \"%s\"", csvPath);
            System.exit(2);
        }

        FileReader in = new FileReader(csvPath);
        String[] HEADERS = {VARIANT_ID, PRODUCT_ID, SIZE_LABEL, PRODUCT_NAME, BRAND, COLOR, AGE_GROUP, GENDER, SIZE_TYPE, PRODUCT_TYPE };

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .setSkipHeaderRecord(true)
                .build();

        Iterable<CSVRecord> records = csvFormat.parse(in);

        // @TODO: Why converting an iterator to spliterator??? Java libs ehh...
        Spliterator<CSVRecord> csvSpliterator = records.spliterator();



        CSVToSKUMapper mapper = new CSVToSKUMapper();
        LanguageDetector languageDetector = new LanguageDetector();

        /**
         * @README
         * Example with JAVA streams.
         * IMHO: PITA...
         *  The assigment says "a Java application" what I understand as "pls. use the Java language".
         *  ... however I'd consider using other languages within the Java ecosystem such as Kotlin or Scala...
         *       for a reason of functional approach, data flow libs (Kotlin flows, Apache Spark etc.) and ofc. Nullability.
         *
         *  Stick with the Java language I should have give a try to the Apache Beam :)
         *
         *  Regardless of only 31M records are given... Presumably the solution should be scalable both horizontally and vertically
         *    With that in Mind I expect from my solution to handle concurrency and parallel execution.
         *
         *    Idempotency was not mentioned in the assigment description, however I expect to have that as well.
         *    ... even tough achieve it might be way more tricky and would require certain tradeoffs (business decisions).
         *
         * @TODO: Assemble this in a factory
         */
        StreamSupport.stream(csvSpliterator, false)
                .limit(100)
                .filter(mapper::filter)
                .map(mapper::map)
               // .filter(languageDetector::filter)
               // .map(languageDetector::map)
                .forEach(sku -> {
                        System.out.println(sku.brand().name().value());
                        // @TODO: Pass to SKUSink to persist in DB.
                });

        System.out.println("Done");
    }
}
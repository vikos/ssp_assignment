package org.vikos;

import org.apache.commons.cli.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

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
        String[] HEADERS = { "variant_id", "product_id", "size_label", "product_name", "brand", "color", "age_group", "gender", "size_type", "product_type" };

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .setSkipHeaderRecord(true)
                .build();

        Iterable<CSVRecord> records = csvFormat.parse(in);

        // @TODO: Why converting an iterator to spliterator??? Java libs ehh...
        Spliterator<CSVRecord> csvSpliterator = records.spliterator();

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ssp_assignment", "ssp_assigment", "ssp_assigment");

        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO test_categories (path) VALUES (?)");


        StreamSupport.stream(csvSpliterator, false).forEach(
                row -> {
                    String productType = row.get("product_type");


                    try {
                        pstmt.setString(1, productType);
                        pstmt.executeUpdate();
                        System.out.println("Inserted: " + productType);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        System.out.println("Done");
    }
}
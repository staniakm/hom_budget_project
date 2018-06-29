package com.mariusz.home_budget.helpers;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class CsvParser {

    public CsvParser(MultipartFile filename) {
        printCsv(filename);
    }

    private void printCsv(MultipartFile fileName) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileName.getInputStream()));

                CSVParser csvParser = new CSVParser(reader, CSVFormat.newFormat(';'));
        ) {
            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord.size());
                String name = csvRecord.get(0);
                String email = csvRecord.get(1);
                String phone = csvRecord.get(2);
                String country = csvRecord.get(3);

                System.out.println("Record No - " + csvRecord.getRecordNumber());
                System.out.println("---------------");
                System.out.println("First name : " + name);
                System.out.println("Last name : " + email);
                System.out.println("Age : " + phone);
                System.out.println("Address : " + country);
                System.out.println("---------------\n\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

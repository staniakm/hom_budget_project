package com.mariusz.home_budget.helpers;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.MoneyHolder;
import com.mariusz.home_budget.entity.form.MoneyFlowForm;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    private MultipartFile file;
    private final AppUser user;
    private final String holder;


    public CsvParser(MultipartFile filename, AppUser user, String holder) {
        this.file = filename;
        this.user = user;
        this.holder = holder;
    }

    public List<MoneyFlowForm> printCsv() {
        List<MoneyFlowForm> moneyFlowForms = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));

                CSVParser csvParser = new CSVParser(reader
                        , CSVFormat.newFormat(';').withFirstRecordAsHeader());

        ) {
            for (CSVRecord csvRecord : csvParser) {
                System.out.println(csvRecord.size());
                if (csvRecord.size()==5) {
                    LocalDate date;
                    String dateRead = csvRecord.get("date");
                    String category = csvRecord.get("category");
                    String description = csvRecord.get("description");
                    String amount = csvRecord.get("amount");
                    String type = csvRecord.get("type");
                    try {
                        date = LocalDate.parse(dateRead);
                    }catch (DateTimeParseException ex){
                        System.out.println("Date parse fail");
                        continue;
                    }

                    System.out.println("Record No - " + csvRecord.getRecordNumber());
                    System.out.println("---------------");
                    System.out.println("Category : " + category);
                    System.out.println("Date : " + date);
                    System.out.println("description : " + description);
                    System.out.println("amount : " + amount);
                    System.out.println("Type : " + type);
                    System.out.println("---------------\n\n");



                    if (type.equalsIgnoreCase("income")
                            || type.equalsIgnoreCase("expense")){

                        MoneyFlowForm moneyFlowForm = new MoneyFlowForm();
                        moneyFlowForm.setAmount(amount);
                        moneyFlowForm.setCategory(category);
                        moneyFlowForm.setDescription(description);
                        moneyFlowForm.setOperation(type);
                        moneyFlowForm.setDate(date);
                        moneyFlowForm.setUser(user);
                        moneyFlowForm.setMoneyHolder(holder);

                        moneyFlowForms.add(moneyFlowForm);

                    }

                }
                }
        } catch (IOException e) {
            System.out.println(e);

        }
        return moneyFlowForms;
    }


}

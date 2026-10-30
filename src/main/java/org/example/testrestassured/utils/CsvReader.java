package org.example.testrestassured.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    public static Object[][] getData(String fileName) {
        List<Object[]> dataList = new ArrayList<>();
        InputStream inputStream = CsvReader.class.getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new RuntimeException("Файл не найден.");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 || line.trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 6) {
                    dataList.add(new Object[]{
                            values[0].trim(),
                            values[1].trim(),
                            values[2].trim(),
                            values[3].trim(),
                            Integer.parseInt(values[4].trim()),
                            values[5].trim()
                    });
                } else {
                    System.err.println("Строка пропущена " + lineNumber + " Ожидается 6 колонок, найдено: " + values.length);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении CSV файла: " + fileName, e);
        }
        return dataList.toArray(new Object[0][]);
    }
}

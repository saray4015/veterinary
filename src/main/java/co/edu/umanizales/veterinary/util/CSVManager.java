package co.edu.umanizales.veterinary.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;
import com.opencsv.bean.CsvBindByName;

@Component
public class CSVManager<T> {
    
    private static final String DATA_DIR = "data/";
    
    static {
        // Crear el directorio data/ si no existe
        new File(DATA_DIR).mkdirs();
    }
    
    public List<T> readFromCSV(String fileName, Class<T> clazz) {
        try (Reader reader = Files.newBufferedReader(Paths.get(DATA_DIR + fileName));
             CSVReader csvReader = new CSVReader(reader)) {
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clazz);

            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(csvReader)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withThrowExceptions(false)
                    .build();

            try {
                return csvToBean.parse();
            } catch (RuntimeException ex) {
                // Si hay líneas malformadas, evitar que la app caiga al iniciar
                return Collections.emptyList();
            }
        } catch (IOException e) {
            // Si el archivo no existe, retornar lista vacía
            return Collections.emptyList();
        }
    }
    
    public void writeToCSV(String fileName, List<T> items) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(DATA_DIR + fileName));
             CSVWriter csvWriter = new CSVWriter((Writer) writer)) {
            // Build header from @CsvBindByName annotations of the bean class
            List<String> headers = new ArrayList<>();
            Class<?> clazz = null;
            if (items != null && !items.isEmpty()) {
                clazz = items.get(0).getClass();
            }
            if (clazz != null) {
                for (Field f : clazz.getDeclaredFields()) {
                    CsvBindByName bind = f.getAnnotation(CsvBindByName.class);
                    if (bind != null) {
                        String col = bind.column();
                        headers.add((col != null && !col.isEmpty()) ? col : f.getName());
                    }
                }
            }
            // Write header row if we detected any @CsvBindByName fields
            if (!headers.isEmpty()) {
                csvWriter.writeNext(headers.toArray(new String[0]));
                csvWriter.flush();
            }

            // Now write bean rows
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withSeparator(',')
                    .build();
            beanToCsv.write(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

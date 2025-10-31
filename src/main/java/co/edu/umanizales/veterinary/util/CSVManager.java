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

@Component
public class CSVManager<T> {
    
    private static final String DATA_DIR = "data/";
    
    static {
        // Crear el directorio data/ si no existe
        new File(DATA_DIR).mkdirs();
    }
    
    public List<T> readFromCSV(String fileName, Class<T> clazz) {
        try (Reader reader = Files.newBufferedReader(Paths.get(DATA_DIR + fileName))) {
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clazz);
            
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            
            return csvToBean.parse();
        } catch (IOException e) {
            // Si el archivo no existe, retornar lista vacía
            return Collections.emptyList();
        }
    }
    
    public void writeToCSV(String fileName, List<T> items) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(DATA_DIR + fileName))) {
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(',')
                    .build();
            
            beanToCsv.write(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

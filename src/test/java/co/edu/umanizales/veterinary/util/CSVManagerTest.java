package co.edu.umanizales.veterinary.util;

import co.edu.umanizales.veterinary.model.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVManagerTest {

    private CSVManager<Owner> csvManager;
    private final String testFileName = "test_owners.csv";
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        csvManager = new CSVManager<>();
        // Configurar el directorio temporal para pruebas
        ReflectionTestUtils.setField(csvManager, "DATA_DIR", tempDir.toString() + "/");
    }
    
    @Test
    void writeAndReadCSV_ShouldMaintainDataIntegrity() throws IOException {
        // Crear datos de prueba
        Owner owner1 = new Owner();
        owner1.setId("1");
        owner1.setName("Juan Pérez");
        owner1.setEmail("juan@example.com");
        owner1.setPhone("1234567890");
        owner1.setAddress("Calle 123");
        
        Owner owner2 = new Owner();
        owner2.setId("2");
        owner2.setName("María Gómez");
        owner2.setEmail("maria@example.com");
        owner2.setPhone("9876543210");
        owner2.setAddress("Carrera 45 #67-89");
        
        List<Owner> originalList = Arrays.asList(owner1, owner2);
        
        // Escribir a CSV
        csvManager.writeToCSV(testFileName, originalList);
        
        // Leer del CSV
        List<Owner> readList = csvManager.readFromCSV(testFileName, Owner.class);
        
        // Verificar que los datos se mantienen iguales
        assertNotNull(readList);
        assertEquals(originalList.size(), readList.size());
        
        for (int i = 0; i < originalList.size(); i++) {
            Owner original = originalList.get(i);
            Owner read = readList.get(i);
            
            assertEquals(original.getId(), read.getId());
            assertEquals(original.getName(), read.getName());
            assertEquals(original.getEmail(), read.getEmail());
            assertEquals(original.getPhone(), read.getPhone());
            assertEquals(original.getAddress(), read.getAddress());
        }
    }
    
    @Test
    void readFromCSV_WithNonExistentFile_ShouldReturnEmptyList() {
        String nonExistentFile = "non_existent.csv";
        List<Owner> result = csvManager.readFromCSV(nonExistentFile, Owner.class);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void writeToCSV_ShouldCreateFile() {
        // Crear un solo propietario para la prueba
        Owner owner = new Owner();
        owner.setId("1");
        owner.setName("Test Owner");
        
        // Escribir a CSV
        csvManager.writeToCSV(testFileName, List.of(owner));
        
        // Verificar que el archivo se creó
        File file = new File(tempDir.toFile(), testFileName);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }
    
    @Test
    void writeToCSV_WithEmptyList_ShouldCreateFileWithHeaderOnly() {
        // Escribir lista vacía
        csvManager.writeToCSV(testFileName, List.of());
        
        // Verificar que el archivo solo contiene el encabezado
        File file = new File(tempDir.toFile(), testFileName);
        assertTrue(file.exists());
        
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            assertEquals(1, lines.size()); // Solo el encabezado
            assertTrue(lines.get(0).contains("id,name,email,phone,address"));
        } catch (IOException e) {
            fail("Error al leer el archivo de prueba", e);
        }
    }
    
    @Test
    void readFromCSV_WithMalformedFile_ShouldHandleGracefully() throws IOException {
        // Crear un archivo CSV mal formado
        File malformedFile = new File(tempDir.toFile(), "malformed.csv");
        Files.write(malformedFile.toPath(), List.of(
            "id,name,email,phone,address",
            "1,Test User,test@example.com,1234567890,Test Address",
            "2,Malformed,missing,fields"  // Fila con campos faltantes
        ));
        
        // Intentar leer el archivo mal formado
        List<Owner> result = csvManager.readFromCSV("malformed.csv", Owner.class);
        
        // Debería devolver solo las filas válidas
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getName());
    }
}

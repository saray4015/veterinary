# Ejemplos de Estructura de Datos CSV

Este documento describe la estructura de los archivos CSV utilizados para la persistencia de datos en el sistema de gestión veterinaria.

## Estructura de Archivos

### 1. owners.csv
```csv
id,name,email,phone,address
1,Juan Pérez,juan@example.com,1234567890,Calle 123
2,María Gómez,maria@example.com,9876543210,Carrera 45 #67-89
```

### 2. veterinarians.csv
```csv
id,name,email,phone,address,licenseNumber
3,Dr. Carlos López,carlos@veterinary.com,5551234567,Av. Principal #100,VET12345
4,Dra. Ana Torres,ana@veterinary.com,5559876543,Calle 80 #45-67,VET67890
```

### 3. pets.csv
```csv
id,name,species,breed,birthDate,ownerId
1,Firulais,DOG,Labrador,2018-05-15,1
2,Michi,CAT,Siames,2020-02-20,2
```

### 4. appointments.csv
```csv
id,dateTime,petId,veterinarianId,serviceType,notes,status,cost
1,2025-11-01T10:00:00,1,3,CONSULTATION,Primera consulta,SCHEDULED,50.0
2,2025-11-02T15:30:00,2,4,VACCINATION,Vacuna anual,SCHEDULED,30.0
```

### 5. medical_records.csv
```csv
id,petId,veterinarianId,date,diagnosis,treatmentNotes
1,1,3,2025-10-30,Control anual,Estado de salud óptimo
```

### 6. treatments.csv
```csv
id,name,description,startDate,endDate,isCompleted,cost
1,Desparasitación,Tratamiento antiparasitario,2025-11-01,2025-11-15,false,25.0
```

### 7. services.csv
```csv
id,name,description,type,baseCost,durationMinutes
1,Consulta General,Consulta de rutina,CONSULTATION,50.0,30
2,Vacunación,Aplicación de vacunas,VACCINATION,30.0,15
```

### 8. medications.csv
```csv
id,name,description,dosage,quantity,unitPrice,expirationDate,manufacturer
1,Antiparasitario,Antiparasitario para perros,1 tableta cada 10kg,100,5.99,2026-12-31,Pfizer
```

### 9. invoices.csv
```csv
id,dateTime,ownerId,subtotal,tax,total,isPaid,paymentMethod
1,2025-10-30T16:45:00,1,75.0,14.25,89.25,true,CREDIT_CARD
```

### 10. specialties.csv
```csv
id,name,description,consultationFee
1,Cirugía,Especialista en cirugía veterinaria,100.0
2,Dermatología,Especialista en enfermedades de la piel,80.0
```

## Notas Importantes

1. Los archivos se guardan en la carpeta `data/` en el directorio raíz de la aplicación.
2. Los archivos se crean automáticamente al guardar datos si no existen.
3. Los campos de fecha deben seguir el formato ISO-8601 (YYYY-MM-DD).
4. Los campos de fecha y hora deben seguir el formato ISO-8601 (YYYY-MM-DDTHH:MM:SS).
5. Los valores booleanos se guardan como `true` o `false` en minúsculas.
6. Los valores numéricos deben usar punto (.) como separador decimal.

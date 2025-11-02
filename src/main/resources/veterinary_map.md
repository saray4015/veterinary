# 🏥 Veterinary System - Concept Map

```
                                                                 +------------------+
                                                                 |                  |
                                                             +---|   Veterinarian   |
                                                             |   |                  |
                                                             |   +--------+---------+
                                                             |            |
                                                             |            | attends
                                                             |            v
+-------------+       +-------------+       +---------------+|   +--------+----------+
|             | owns  |             | has   |               |    |                   |
|    Owner    |------>|   Animal    |------>|  Appointment  |<---|  VeterinaryService  |
|             | 1    *|             | 1    *|               |uses|                   |
+-------------+       +-------------+       +-------+-------+    +----------+--------+
                                                      |                      |
                                                      | generates           | has type
                                                      v                      v
                                              +-------+-------+    +--------+--------+
                                              |               |    |                 |
                                              |    Invoice    |    |   ServiceType   |
                                              |               |    |                 |
                                              +-------+-------+    +-----------------+
                                                      |
                                                      | includes
                                       +--------------+--------------+
                                       |                             |
                              +--------+--------+          +---------+---------+
                              |                 |          |                   |
                              |   Treatment    |          |     Medication    |
                              |  • ID          |          |    • ID           |
                              |  • Name        |          |    • Name         |
                              |  • Description |          |    • Description  |
                              |  • Cost        |          |    • Dosage       |
                              |  • apply()     |          |    • Frequency    |
                              |                 |          |    • Cost         |
                              +-----------------+          |    • getPrescrip() |
                                                          |                   |
                                                          +-------------------+
```

## 📌 Relaciones Principales

1. **Owner (Dueño)**
   - ⮕ **Animal** (1 a muchos)
     - Un dueño puede tener varios animales
     - Cada animal tiene exactamente un dueño

2. **Animal**
   - ⮕ **Appointment** (1 a muchos)
     - Un animal puede tener múltiples citas
     - Cada cita es para exactamente un animal

3. **Appointment (Cita)**
   - ⮕ **VeterinaryService** (muchos a 1)
     - Usa un servicio veterinario
   - ⮕ **Invoice** (1 a 1)
     - Genera una factura
   - ⮕ **Veterinarian** (muchos a 1)
     - Atendida por un veterinario

4. **VeterinaryService (Servicio)**
   - ⮕ **ServiceType** (muchos a 1)
     - Tiene un tipo de servicio

5. **Invoice (Factura)**
   - ⮕ **Treatment** (1 a muchos)
     - Incluye tratamientos
   - ⮕ **Medication** (1 a muchos)
     - Incluye medicamentos

## 📋 Leyenda

- **Cajas**: Representan las entidades principales
- **Flechas (--->)**: Muestran las relaciones
- **Números (1, &#42;)**: Cardinalidad de las relaciones
- **Puntos (•)**: Atributos o métodos importantes

## 🔄 Relaciones de Cardinalidad

- **1 a 1**: Una instancia se relaciona con exactamente una instancia
- **1 a muchos (1:&#42;)**: Una instancia se relaciona con múltiples instancias
- **Muchos a 1 (&#42;:1)**: Múltiples instancias se relacionan con una instancia

## 💡 Notas Adicionales

- El diagrama muestra las relaciones principales del sistema
- Cada entidad tiene sus atributos clave
- Las relaciones están etiquetadas con su tipo y cardinalidad

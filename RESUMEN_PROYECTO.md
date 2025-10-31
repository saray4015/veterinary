# Resumen del Proyecto: Sistema de Gestión de Clínica Veterinaria

## Visión General

Este proyecto implementa un sistema completo de gestión para clínicas veterinarias, diseñado para facilitar la administración de pacientes, citas, historiales médicos, tratamientos y facturación. El sistema está desarrollado con tecnologías modernas siguiendo las mejores prácticas de desarrollo de software.

## Características Principales

### Gestión de Clientes y Mascotas
- Registro y seguimiento de dueños de mascotas
- Historial completo de cada mascota
- Control de vacunas y tratamientos

### Gestión Médica
- Programación de citas
- Historial médico detallado
- Recetas y tratamientos personalizados
- Control de medicamentos

### Administración
- Facturación de servicios
- Gestión de inventario
- Reportes y estadísticas

### Especialidades Veterinarias
- Asignación de especialistas
- Control de disponibilidad
- Seguimiento de casos especiales

## Tecnologías Utilizadas

### Backend
- **Java 23**: Lenguaje de programación principal
- **Spring Boot 3.3.0**: Framework para aplicaciones empresariales
- **Lombok**: Reducción de código boilerplate
- **OpenCSV**: Manejo de archivos CSV
- **H2 Database**: Base de datos en memoria para desarrollo

### Herramientas de Desarrollo
- **Maven**: Gestión de dependencias
- **Git**: Control de versiones
- **Lombok**: Generación de código

## Estructura del Proyecto

```
veterinary/
├── src/
│   ├── main/
│   │   ├── java/co/edu/umanizales/veterinary/
│   │   │   ├── config/         # Configuraciones
│   │   │   ├── controller/     # Controladores REST
│   │   │   ├── model/          # Modelos de datos
│   │   │   ├── repository/     # Repositorios
│   │   │   ├── service/        # Lógica de negocio
│   │   │   └── util/           # Utilidades
│   │   └── resources/          # Recursos
│   └── test/                   # Pruebas
├── data/                       # Datos persistentes
└── logs/                       # Archivos de registro
```

## Instalación y Ejecución

1. **Requisitos Previos**
   - Java 23 o superior
   - Maven 3.6 o superior

2. **Clonar el Repositorio**
   ```bash
   git clone https://github.com/tu-usuario/veterinary.git
   cd veterinary
   ```

3. **Compilar el Proyecto**
   ```bash
   mvn clean install
   ```

4. **Ejecutar la Aplicación**
   ```bash
   mvn spring-boot:run
   ```

5. **Acceder a la Aplicación**
   - API: http://localhost:8080/api
   - H2 Console: http://localhost:8080/h2-console
   - Swagger UI: http://localhost:8080/swagger-ui.html

## Documentación Adicional

- [Guía de Implementación](GUIA_IMPLEMENTACION.md)
- [Ejemplos de Datos CSV](EJEMPLO_DATOS_CSV.md)
- [Documentación de la API](http://localhost:8080/v3/api-docs)

## Contribución

Las contribuciones son bienvenidas. Por favor, lee las directrices de contribución antes de enviar un pull request.

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Contacto

Para consultas o soporte, contacta con el equipo de desarrollo en: soporte@veterinary.com

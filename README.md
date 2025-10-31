# Sistema de Gestión de Clínica Veterinaria

Este es un sistema de gestión para clínicas veterinarias desarrollado con Spring Boot y Java 23. El sistema permite administrar dueños, mascotas, citas, historiales médicos, tratamientos, medicamentos, servicios, facturas y especialidades de los veterinarios.

## Características

- Gestión completa de mascotas y sus dueños
- Programación y seguimiento de citas
- Historial médico detallado para cada mascota
- Control de inventario de medicamentos
- Facturación de servicios y productos
- Especialidades de veterinarios
- Persistencia en archivos CSV
- API RESTful documentada

## Requisitos

- Java 23 o superior
- Maven 3.6 o superior
- Spring Boot 3.3.0

## Instalación

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/veterinary.git
   cd veterinary
   ```

2. Compilar el proyecto:
   ```bash
   mvn clean install
   ```

3. Ejecutar la aplicación:
   ```bash
   mvn spring-boot:run
   ```

4. Acceder a la aplicación en: http://localhost:8080

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
│   │   └── resources/          # Recursos (propiedades, plantillas, etc.)
│   └── test/                   # Pruebas
├── .gitignore
├── pom.xml
└── README.md
```

## Documentación de la API

La documentación de la API está disponible en formato OpenAPI (Swagger) en:
http://localhost:8080/swagger-ui.html

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Contribución

Las contribuciones son bienvenidas. Por favor, lee las directrices de contribución antes de enviar un pull request.

## Contacto

Para consultas o soporte, contacta con el equipo de desarrollo en: soporte@veterinary.com

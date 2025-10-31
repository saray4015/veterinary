Enunciado del proyecto:
Desarrollar una API REST en Java (versión superior a Java 23) utilizando los frameworks Spring
Boot y Lombok, que gestione la información de un sistema de administración para una clínica
veterinaria.
La aplicación deberá implementar un CRUD completo para al menos 10 clases relacionadas con
la gestión de la veterinaria, tales como: Mascota, Dueño, Veterinario, Cita, Tratamiento,
HistoriaClínica, Medicamento, Factura, Servicio, Especialidad, entre otras.
Los datos deberán almacenarse en un archivo separado por comas (CSV) en lugar de una base de
datos tradicional.
El diseño debe evidenciar de forma clara los principios de la Programación Orientada a Objetos
(POO):
 Encapsulamiento: protección de atributos mediante modificadores de acceso y uso de
getters/setters.
 Herencia: jerarquías entre clases (por ejemplo, Persona → Veterinario y Dueño).
 Polimorfismo: métodos sobrecargados o sobrescritos en clases derivadas.
 Interfaces: contratos funcionales como Atendible, Pagable o Registrable.
 Composición y agregación: relaciones entre entidades (por ejemplo, una Cita contiene una
Mascota y un Veterinario).
Además, se deberán incluir records, enumeradores y clases abstractas cuando el diseño lo
requiera (por ejemplo, un record para datos inmutables como Dirección, o un enum para tipos de
servicio o especie animal)
# MicroServicio Fiesta - Spring Boot + Oracle + JPA

Microservicio desarrollado con Spring Boot para la gestión de fiestas dentro de una arquitectura de microservicios.

Este servicio permite registrar, consultar, actualizar y eliminar fiestas, utilizando persistencia con Oracle Database y Spring Data JPA.

---

# Objetivo del Proyecto

Este microservicio permite:

- Registrar fiestas
- Consultar fiestas por ID
- Listar fiestas
- Actualizar información de fiestas
- Eliminar fiestas
- Realizar búsquedas personalizadas
- Persistir información utilizando Oracle Database
- Aplicar validaciones con Jakarta Validation
- Implementar arquitectura en capas

---

# Tecnologías Utilizadas

| Tecnología           | Descripción                       |
| -------------------- | --------------------------------- |
| Java 21              | Lenguaje principal                |
| Spring Boot          | Framework principal               |
| Spring Web MVC       | Desarrollo de APIs REST           |
| Spring Data JPA      | Persistencia ORM                  |
| Hibernate            | ORM                               |
| Oracle Database      | Base de datos                     |
| Oracle Wallet        | Conexión segura a Oracle Cloud    |
| Jakarta Validation   | Validaciones de DTOs              |
| Lombok               | Reducción de boilerplate          |
| Maven                | Gestión de dependencias           |
| Spring Boot DevTools | Reinicio automático en desarrollo |

---

# Dependencias Agregadas desde start.spring.io

Al crear el proyecto en:

```text
https://start.spring.io
```

se agregaron las siguientes dependencias desde la interfaz gráfica:

- Spring Web MVC
- Spring Data JPA
- Validation
- Lombok
- Spring Boot DevTools

---

## Spring Web MVC

Permite:

- Crear APIs REST
- Utilizar `@RestController`
- Utilizar `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- Serialización automática JSON
- Tomcat embebido

---

## Spring Data JPA

Permite:

- Persistencia ORM
- Uso de `JpaRepository`
- Mapear entidades con `@Entity`
- Consultas automáticas
- Integración con Hibernate

---

## Validation

Permite:

- Validar DTOs
- Utilizar `@Valid`
- Utilizar validaciones como:
  - `@NotBlank`
  - `@NotNull`
  - `@Positive`
  - `@FutureOrPresent`

---

## Lombok

Permite:

- Reducir código repetitivo
- Generar getters y setters automáticamente
- Generar constructores automáticamente
- Simplificar entidades y DTOs

---

## Spring Boot DevTools

Permite:

- Reinicio automático
- Hot reload
- Mejor experiencia de desarrollo

---

# Dependencias Agregadas Manualmente

Las siguientes dependencias fueron agregadas manualmente al proyecto.

---

## Oracle JDBC Driver

```xml
<dependency>
    <groupId>com.oracle.database.jdbc</groupId>
    <artifactId>ojdbc11</artifactId>
    <version>21.9.0.0</version>
</dependency>
```

### ¿Qué hace?

Permite:

- Conectarse a Oracle Database
- Utilizar JDBC con Java 21
- Integrarse con Hibernate y Spring Data JPA

---

## Oracle Wallet Security

```xml
<dependency>
    <groupId>com.oracle.database.security</groupId>
    <artifactId>oraclepki</artifactId>
    <version>21.9.0.0</version>
</dependency>
```

```xml
<dependency>
    <groupId>com.oracle.database.security</groupId>
    <artifactId>osdt_core</artifactId>
    <version>21.9.0.0</version>
</dependency>
```

```xml
<dependency>
    <groupId>com.oracle.database.security</groupId>
    <artifactId>osdt_cert</artifactId>
    <version>21.9.0.0</version>
</dependency>
```

### ¿Qué hacen?

Permiten:

- Conexión segura mediante Oracle Wallet
- Comunicación SSL con Oracle Cloud
- Autenticación segura con la base de datos

---

# Arquitectura del Proyecto

El proyecto utiliza arquitectura en capas.

```text
src/main/java/cl/duoc/AppFiesta
├── controller      # Endpoints REST
├── dto             # DTO Request y Response
│   ├── request
│   └── response
├── model           # Entidades JPA
├── repository      # Acceso a datos
├── service         # Lógica de negocio
└── AppFiestaApplication.java
```

---

# Flujo de Funcionamiento

## Crear una Fiesta

1. El cliente realiza una solicitud HTTP:

```http
POST /api/v1/fiesta
```

2. El Controller recibe el request

3. Se validan los datos utilizando Jakarta Validation

4. El Service:

- Construye la entidad
- Valida reglas de negocio
- Guarda la información utilizando JPA

5. Hibernate genera el SQL automáticamente

6. Oracle almacena la información

7. Se retorna un DTO Response

---

# Endpoints Principales

---

## Obtener una fiesta por ID

```http
GET /api/v1/fiesta/{id}
```

---

## Obtener todas las fiestas

```http
GET /api/v1/fiesta
```

---

## Crear una fiesta

```http
POST /api/v1/fiesta
```

### Ejemplo Request

```json
{
  "nombre": "Fiesta Electrónica",
  "tipoDeFiesta": "Electronica",
  "fechaRealizacion": "20-12-2026",
  "direccion": "Av Providencia 123",
  "capacidad": 500,
  "comunaId": 1
}
```

---

## Actualizar una fiesta

```http
PUT /api/v1/fiesta/{id}
```

---

## Eliminar una fiesta

```http
DELETE /api/v1/fiesta/{id}
```

---

## Búsquedas personalizadas

```http
GET /api/v1/fiesta/buscar
```

---

# DTOs

## Request

### FiestaCreateRequest

Utilizado para registrar fiestas.

Validaciones utilizadas:

- `@NotBlank`
- `@NotNull`
- `@Positive`
- `@FutureOrPresent`

---

### FiestaUpdateRequest

Utilizado para actualizar fiestas existentes.

---

## Response

### FiestaResponse

DTO utilizado para responder información al cliente.

---

# Modelo de Datos

## Entidades principales

### Fiesta

Representa una fiesta registrada en el sistema.

---

### Comuna

Representa una comuna asociada a una fiesta.

---

# Base de Datos

Motor utilizado:

```text
Oracle Database
```

El proyecto utiliza:

- Oracle JDBC Driver
- Hibernate
- Oracle Wallet
- Spring Data JPA

---

# Configuración Oracle Wallet

El proyecto utiliza Oracle Wallet para conexiones seguras.

Ruta utilizada:

```properties
spring.datasource.url=jdbc:oracle:thin:@bdejemplofiestas_high?TNS_ADMIN=/ruta/Wallet/
```

---

# Configuración del Proyecto

Archivo principal:

```properties
spring.profiles.active=dev
```

---

## application-dev.properties

```properties
spring.application.name=Ventas (dev)

spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.datasource.url=jdbc:oracle:thin:@bdejemplofiestas_high?TNS_ADMIN=/ruta/Wallet/

spring.datasource.username=ADMIN
spring.datasource.password=PASSWORD

spring.jpa.hibernate.ddl-auto=none

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.OracleDialect

server.port=8081
```

---

# Ejecución del Proyecto

## macOS / Linux

```bash
./mvnw spring-boot:run
```

---

## Windows

```bash
mvnw.cmd spring-boot:run
```

---

# Build del Proyecto

```bash
./mvnw clean package
```

---

# Ejecutar JAR

```bash
java -jar target/*.jar
```

---

# Conceptos Implementados

- REST API
- DTO
- JPA
- Hibernate
- Oracle Database
- Oracle Wallet
- Arquitectura en capas
- Repository Pattern
- Service Layer
- Dependency Injection
- Bean Validation
- CRUD REST
- ResponseEntity
- Validaciones Jakarta

---

# Buenas Prácticas Implementadas

- Arquitectura en capas
- Separación de responsabilidades
- Uso de DTOs
- Validaciones con Jakarta
- Uso de JPA Repository
- Configuración por perfiles
- Uso de Lombok
- Uso de ResponseEntity
- Persistencia desacoplada mediante Hibernate

---

# Mejoras Futuras

- Manejo global de excepciones (`@RestControllerAdvice`)
- Documentación Swagger/OpenAPI
- Integración con Spring Security
- JWT Authentication
- Dockerización
- Integración continua con GitHub Actions
- Pruebas unitarias con JUnit y Mockito
- Logs estructurados
- Paginación y ordenamiento
- Implementación de Flyway
- Implementación de WebClient para integración con otros microservicios

---

# Autor

Proyecto académico desarrollado por **Daniel Riquelme**
como parte del aprendizaje de:

- Spring Boot
- Microservicios
- JPA
- Hibernate
- Oracle Database
- Arquitectura REST
- DTOs
- Validaciones con Jakarta
- Arquitectura en capas

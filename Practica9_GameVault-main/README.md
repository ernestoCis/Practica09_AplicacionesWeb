# GameVault - Práctica de Clase: Thymeleaf & Uploads

Bienvenido profesor. Esta es la guía didáctica para la práctica de 1.5 horas sobre el uso de Fragmentos en Thymeleaf y la subida de archivos (imágenes) en Spring Boot.

## Codigo Base
https://drive.google.com/file/d/1pgo2F9Hec3hw7-scU4BWR3mc1aMsPTBq/view?usp=sharing

## 1. Configuración Inicial del Proyecto (Spring Initializr)

Antes de empezar, asegúrate de mostrar a los alumnos cómo crear el proyecto en [start.spring.io](https://start.spring.io/).
Los parámetros basados en el ejemplo proporcionado son:
- **Project:** Maven
- **Language:** Java (17 o 21)
- **Spring Boot:** 3.4.x (o la versión estable más reciente)
- **Group:** `com.example`
- **Artifact:** `GameVault`
- **Dependencies:** 
  - Spring Web
  - Thymeleaf
  - Spring Data JPA
  - MySQL Driver
  - Spring Security
  - Validation
  - Spring Boot DevTools
  - Lombok

*Nota para el profesor:* Aunque agregamos dependencias de base de datos y seguridad, **para esta práctica trabajaremos con datos en memoria** y dejaremos las rutas abiertas. Esto prepara el terreno para futuras clases.

## 2. Cronograma de la Práctica (1.5 horas)

### Parte 1: Fragmentos y Layouts (40 min)
1. **Introducción Teórica (10 min):**
   - Mostrar `/fragments-demo`.
   - Explicar diferencias clave:
     - `th:replace`: Reemplaza la etiqueta host por el fragmento.
     - `th:insert`: Inserta el fragmento *dentro* de la etiqueta host.
     - `th:include`: (Obsoleto en Thymeleaf 3) Inserta el *contenido* del fragmento.
2. **Crear Layout Maestro (15 min):**
   - Mostrar `src/main/resources/templates/layout/base.html`.
   - Explicar el patrón de decoración `th:fragment="layout(title, content)"`.
3. **Aplicar Layout a la Vista de Juegos (15 min):**
   - Mostrar `src/main/resources/templates/juegos.html`.
   - Explicar cómo le pasamos parámetros al layout.

### Parte 2: Subida de Archivos / Formularios (50 min)
1. **Configuración de Límites (5 min):**
   - Revisar `application.properties` y explicar `spring.servlet.multipart.max-file-size`.
   - **Tip:** Mencionar que sin esto, ataques de DDoS llenando el disco duro son más probables.
2. **El Formulario HTML (10 min):**
   - Mostrar `src/main/resources/templates/formulario.html`.
   - **Crucial:** Recalcar `enctype="multipart/form-data"` (sin esto, el archivo no viaja) y `accept="image/*"` (restricción visual, no de seguridad).
3. **El Controlador y Lógica de Guardado (20 min):**
   - Revisar `GameController.java`.
   - Mostrar cómo recibir `MultipartFile file`.
   - Explicar la lógica de creación de la carpeta `static/uploads/` y el guardado con `Files.copy()`.
4. **Guardado en Cloudinary [Opcional/Avanzado] (15 min):**
   - Enseñar el método comentado `uploadToCloudinary` en el controlador.
   - Explicar los pasos teóricos:
     - 1. Agregar dependencia a `pom.xml`:
       ```xml
       <dependency>
           <groupId>com.cloudinary</groupId>
           <artifactId>cloudinary-http44</artifactId>
           <version>1.36.0</version>
       </dependency>
       ```
     - 2. Configurar credenciales (Bean).
     - 3. Usar el SDK (`cloudinary.uploader().upload()`).

---

## 3. Consideraciones de Seguridad (SecurityConfig)

Hemos incluido un archivo `SecurityConfig.java` que deshabilita la seguridad (con `permitAll()`) para todas las rutas y de igual manera deshabilita CSRF y cabeceras de frames. 
**Tip para la clase:** Menciónales que al incluir `spring-boot-starter-security`, Spring automáticamente bloquea todas las rutas y muestra un login, por lo que creamos esta configuración para "apagarlo" temporalmente.

## 4. Solución al Error de JPA (Base de Datos en Memoria H2)

Es altamente probable que si tienes dependencias de base de datos como JPA en el `pom.xml`, la aplicación no quiera arrancar lanzando un error como: `Unable to determine Dialect without JDBC metadata...`.
**Solución aplicada:** 
Para no batallar con exclusiones de clases en Spring Boot, **agregamos la dependencia `h2` al `pom.xml`** y configuramos la conexión en el `application.properties` con `jdbc:h2:mem:gamevaultdb`.
**Tip para la clase:** Explícales a los alumnos que Spring Boot, en su "magia" de autoconfiguración, obliga a que exista una conexión a base de datos si detecta la dependencia JPA. Con H2 engañamos al sistema dándole una base de datos 100% en la memoria RAM que es rapidísima y perfecta para el desarrollo inicial.

## 5. Manejo de Archivos Estáticos (CSS, JS, Imágenes)

Los estilos ahora están extraídos en un archivo propio: `src/main/resources/static/css/styles.css`.
En Spring Boot, todo lo que pongas dentro de la carpeta `static` es público automáticamente.
Para enlazar este CSS en Thymeleaf, en `base.html` usamos:
```html
<link rel="stylesheet" th:href="@{/css/styles.css}">
```
**Tip para la clase:** Haz énfasis en el uso de la arroba `@{}` en Thymeleaf para crear URLs correctas independientemente del contexto de despliegue de la aplicación.

**Problema del 404 en Imágenes Subidas en Runtime:**
Si suben una imagen y al verla marca "404 Not Found" aunque la imagen sí exista físicamente en la carpeta `static/uploads/`, esto ocurre porque Spring Boot empaqueta y sirve la carpeta `static` original en memoria al arrancar. Para evitar que devuelva 404 a los archivos creados "en caliente", incluimos el archivo `WebConfig.java`. Explícales que este archivo le enseña a Spring a leer directamente desde el disco duro (`file:/`) la carpeta de uploads, sirviéndolos de manera instantánea sin reiniciar.

---

## ¡Éxito en tu clase! 🎮
El código está comentado para guiarte durante la explicación.

# API Consultorio

Sistema que permite administrar un consultorio médico

Cuento con los servicios de
- Consulta de medicos
- Consulta de citas
- Registro de citas

## Cómo utilizarlo

Se requiere de java 17 para poder iniciar el proyecto en local, para una mejor experiencia de uso se decidió utilizar H2 como base de datos en memoria además de usar datos de pruebas que se genera de forma automática al iniciar el proyecto.
Se habilitó la interfaz de Open Api para probar directamente el api rest, el servicio se encontrará disponible en la siguiente ruta al iniciar el proyecto en local.

http://localhost:8080/swagger-ui/index.html

## Decisiones de arquitectura

Tome la decisión de utilizar una arquitectura en capas para un desarrollo más rápido dado el tiempo proporcionado. Las capas principales son las siguientes

- api
    - Contiene la definición del api así como sus modelos (dto) utilizados
- config
  - Contiene bens de configuraciones para generar los datos de pruebas
- domain
  - Contiene la representación de la base de datos en clases java
- exception
  - Contiene las diferentes excepciones personalizadas utilizadas además de centralizar en un único punto la gestión de errores.
- mapper
  - Contiene los mapper utilizados para convertir un objeto de un tipo a otro, por ejemplo de entity a dto
- service
  - Contiene la lógica de negocio
- util
  - Contiene diferentes clases de utileria

## Aspectos importantes a destacar

- Gestión centralizada de errores utilizando GlobalExceptionHandler
- Homologación de respuestas del api, se mantiene siempre la misma estructura
- Manejo de códigos de estatus en la respuesta del api
- Separación entre capas para tener identificadas las responsabilidades de cada capa

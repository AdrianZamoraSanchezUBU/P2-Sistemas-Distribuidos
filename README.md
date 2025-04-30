# P2-Sistemas-Distribuidos
Práctica 2 de la asignatura de Sistemas Distribuidos. Consiste en una aplicación web con soporte para gestión de usuarios y pruebas de casos de excepciones.

## Características
### Spring Boot
La aplicación consisten principalmente en un servidor web con Spring Boot, cuenta con las siguientes características:
- Genera las vistas con Thymeleaf.
- Soporta login securizado.
- Aporta un sistema de gestión de usuarios.
- Se comunica con una base de datos MySQL.
- Se comunica con una API en flask.

### MySQL
- Contiene una base de datos llamada practica2 con una tabla de users.
- Contiene información de los usuarios de la app.

### API en Flask
- Contiene un sistema de gestión de archivos, para lectura y escritura.
- Hace llamadas a la base de datos MySQL, soporta el test de conexiones, inserciones, lecturas, etc.
- Hace llamadas a la API de [Pokémon](https://pokeapi.co/)
- Gestiona las excepciones que se puedan generar y las devuelve bajo un formato correcto.

## Uso
Dentro de la app, se puede acceder a un panel de gestión de usuarios, para ello se requiere un login como administrador, el cual podemos hacer con el usuario por defecto "admin" con contraseña "admin".
De la misma forma, para acceder a excepciones para las que se requiere una conexión a una base de datos podemos emplear la base de datos "practica2" y la tabla "users".

Para generar los ejecutables y poner en marcha los contenedores de Docker se deben seguir los siguientes comandos:

### Para generar el jar:
`mvn clean package -DskipTests`

NOTA: es importante saltar los tests puesto que no hay una base de datos aún, esta se genera en el docker compose, pero sin el .jar que se genera con mvn package docker compose fallaría, es decir, de esta forma se evita un error de dependencia circular

### Para levantar los contenedores de mySQL y la app de Spring Boot
`docker-compose up -d --build`

### Para ver el estado de los contenedores
`docker-compose ps`

### Para levantar la APP
`call docker-compose up -d`

## Lanzar todo
Para evitar tener que ejecutar varios comandos para lanzar cada contenedor, tengo un script en bash que crea todo el proyecto y lo levanta:

`.\build.bat`

### Conclusión
La práctica ha sido completada de forma satisfactoria, alcanzado un estado que considero suficientemente completo con respecto a los requisitos que se nos planteaban. Por otro lado, me hubiese gustado tener algo más de tiempo para estudiar el funcionamiento de Spring Boot como framework y poder aplicar algunos estilos más detallados que cubran todas las vistas de la app con igual completitud.

Considero haber aprendido mucho sobre el modelo vista controlador así como la integración de bases de datos (MySQL) y APIs sencillas (tanto propias como de terceros).

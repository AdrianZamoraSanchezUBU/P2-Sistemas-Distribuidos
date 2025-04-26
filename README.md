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
Para generar los ejecutables y poner en marcha los contenedores de Docker se deben seguir los siguientes comandos.

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

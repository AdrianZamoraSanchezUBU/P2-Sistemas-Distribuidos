# P2-Sistemas-Distribuidos
 Práctica 2 de la asignatura de Sistemas Distribuidos. Consiste en una aplicación web con Spring Boot y una API en flask

## Uso
### Para generar el jar:
`mvn clean package -DskipTests`
NOTA: es importante saltar los tests puesto que no hay una base de datos aún, esta se genera en el docker compose, pero sin el .jar que se genera con mvn package docker compose fallaría, es decir, de esta forma se evita un error de dependencia circular

### Para levantar los contenedores de mySQL y la app de Spring Boot
`docker-compose up -d --build`

### Para comprobar que se han levantado ambos contenedores bien
`docker-compose ps`

### Para levantar la APP
`docker-compose up app`

## Lanzar todo
Para evitar tener que ejecutar varios comandos para lanzar cada contenedor, tengo un script en bash que crea el proyecto y lo levanta:
`.\build.bat`
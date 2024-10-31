# Energy Management App
This is a project designed to manage the consumption of all devices of an user.
The project is developed in three iterations:

## Iteration 1
Defining user and devices as entities, defining the architecture and arranging the microservices.

## Build and run
As frameworks, for frontend we used **Angular** with **Typescript**, and for backend we have
chosen **Java** as programming language, with some help from **Spring Boot** framework.

In terms of database and database interaction, we've chosen **Postgresql** (**PgAdmin4** proves to be a good db managing app)
and **Hibernate** (in combination with Spring Boot, it can generate sql queries).

### Local development
#### Frontend - energy-mgmt-frontend - Angular
The project uses some dependencies. For those to be compiled, first and only once run 
```terminal
npm install
```
Then to start the server (which runs in watch mode), run
```terminal
ng serve
```

#### Backend - energy-mgmt-login, user, device - Java Spring Boot
Projects were built on a Maven foundation.

To avoid any inadvertencies, first and only once, regardless of project, run
```maven
mvn clean install
```
Then, to run the project, run
```maven
mvn spring-boot:run -f pom.xml
```

### Docker
Each service (databases included) have been containerized. Meaning that a docker image has been built 
for each one of them and a `docker-compose.yaml` (here `compose.yaml`) specifies how those
containers are grouped.

Here I will not go into details with how to build those images, but to run the docker project:
- open the terminal and change the directory to the root of this repository

Then run
```docker
docker-compose up
```

To stop the container you can
- either `ctrl+C` in current terminal
- or open a new terminal at the same location and run 
```docker
docker-compose down
```



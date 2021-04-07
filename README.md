# Database Browser

This repository contains a small project made using Spring Boot(Spring MVC) 2.4 and Java 11.
It uses an H2 database to persist the list of connections.
The persistence to the H2 DB is made with Spring JPA where all connections are stored.

The connections to MySQL servers are handled with JDBC.
There is a REST API available to browse though Schemas, Tables, Columns and a preview of each table.

There is a docker-compose.yml file for convenience if you want to use it to start a MySQL DB from a Docker container.

---
**Steps to run:**

1. Clone the repositoy
`$ git clone https://github.com/luis-e-lopez/DB-Browser`

2. Download Tomcat 9

3. Configure Tomcat's Deployment Directory to point to `[Project Root Directory]/src/main/webapp`
Create the webapp directory if not present.

4. Configure Tomcat's Context Path to `/`

5. Run Tomcat

6. In your browser navigate to http://localhost:8080/index

7. Two tables are created inside the H2 database during the first execution. To avoid that data to be overwritten, set the value of property hibernate.hbm2ddl.auto to **none** inside the application.properties file `hibernate.hbm2ddl.auto=none`
---

REST API documentation is under way...
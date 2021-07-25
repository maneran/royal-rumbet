# sheepit

### Steps for booting the app :

#### Install list of technologies:

- Docker and Docker compose(Docker desktop for Windows)
- java 8 (https://adoptopenjdk.net/)
- maven 3.6 version or above(https://maven.apache.org/install.html)
  - after installation copy settings.xml under .m2 folder
- node(npm)
- Intellij IDE (Recommended)

####Run the following command in separate terminal

```
docker-compose -f src/main/docker/keycloak-postgres-pgadmin.yml up -d
```

##

pgAdmin4 development url address:
http://localhost:5050/browser/

```
User: admin@pgadmin.org
Password: admin
```

Once logged in create a server with postgres credentials below

##

Postgresql credentials:

```
POSTGRES_DB: keycloak
POSTGRES_USER: keycloak
POSTGRES_PASSWORD: password
```

How to get the Postgres **host** address parameter:

```
docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' <postgres container id>
```

##

Keycloak development url address:
http://localhost:9080/auth/

```
User: admin
Password: Pa55w0rd
```

For facebook and google login, you will need to configure keycloak with social login once.

##

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
npm start
```

http://localhost:9000/

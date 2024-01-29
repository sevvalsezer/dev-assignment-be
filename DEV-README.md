# Transferz Dev Assignment BE
### Sevval Beyza Sezer

## Build & Run

### Database:

<u>**Requirements:**</u>

* Docker

To run application without local database requirements, execute the following command:

    docker-compose up

The command runs a container composed with `postgresql:14` image and creates a database named `dev-assignment-be`.

### Application:

<u>**Requirements:**</u>
* Java 17

The environment variables are already defined in the run configuration.

Run the application by `LOCAL DEV` from IDE.

## REST API Examples

A postman collection is available under `postman` folder in the project root. 

### Create Airport:

```
curl --location 'http://localhost:8080/airports' \
--header 'Content-Type: application/json' \
--data '{
    "name":"Amsterdam Airport Schiphol",
    "code": "AMS",
    "countryCode": "NL"
}'
```

### Get Airports:

```
curl --location 'http://localhost:8080/airports?countryCode=NL'
```

### Create Flight:

```
curl --location 'http://localhost:8080/flights' \
--header 'Content-Type: application/json' \
--data '{
    "code": "SB-9051"
}'
```

### Add Passenger To Flight:

```
curl --location 'http://localhost:8080/flights/SB-9051/add-passenger' \
--header 'Content-Type: application/json' \
--data '{
    "name": "Sevval Beyza Sezer"
}'
```
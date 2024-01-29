CREATE TABLE airport
(
    code         VARCHAR(4)   NOT NULL,
    name         VARCHAR(255) NOT NULL,
    country_code CHAR(2)      NOT NULL,
    CONSTRAINT airport_pk PRIMARY KEY (code),
    UNIQUE (name)
);

CREATE TABLE flight
(
    code                     VARCHAR(20) NOT NULL,
    origin_airport_code      VARCHAR(4)  NOT NULL,
    destination_airport_code VARCHAR(4),
    departure_date_time      TIMESTAMP,
    arrival_date_time        TIMESTAMP,
    CONSTRAINT flight_pk PRIMARY KEY (code),
    CONSTRAINT flight_origin_airport_code_fk FOREIGN KEY (origin_airport_code) REFERENCES airport (code),
    CONSTRAINT flight_destination_airport_code_fk FOREIGN KEY (destination_airport_code) REFERENCES airport (code)
);

CREATE TABLE passenger
(
    id          UUID          NOT NULL,
    name        VARCHAR(1024) NOT NULL,
    flight_code VARCHAR(20)   NOT NULL,
    CONSTRAINT passenger_pk PRIMARY KEY (id),
    CONSTRAINT passenger_flight_code_fk FOREIGN KEY (flight_code) REFERENCES flight (code),
    UNIQUE (name, flight_code)
);

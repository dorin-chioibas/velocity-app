CREATE TABLE Bird (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      color VARCHAR(255) NOT NULL,
                      weight DOUBLE PRECISION,
                      height DOUBLE PRECISION
);

CREATE TABLE Sighting (
                          id SERIAL PRIMARY KEY,
                          bird_id BIGINT NOT NULL,
                          location VARCHAR(255) NOT NULL,
                          date_time TIMESTAMP NOT NULL,
                          CONSTRAINT fk_bird
                              FOREIGN KEY(bird_id)
                                  REFERENCES Bird(id)
);

CREATE TABLE IF NOT EXISTS user_table (
                      personal_id      BIGINT NOT NULL,
                      firstname        VARCHAR(30) NOT NULL,
                      lastname         VARCHAR(30) NOT NULL,
                      date_of_birth    DATE NOT NULL,
                      email            VARCHAR(30) NOT NULL,
                      address          VARCHAR(40),
                      PRIMARY KEY (personal_id)
);

CREATE TABLE IF NOT EXISTS account (
                         id_account         BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         number             BIGINT NOT NULL,
                         password           VARCHAR(50) NOT NULL,
                         balance            DECIMAL(10, 2) NOT NULL,
                         date_of_creation   DATE NOT NULL,
                         type               CHAR(15) NOT NULL,
                         status             ENUM('ACTIVE', 'INACTIVE') NOT NULL,
                         id_user            BIGINT NOT NULL,
                         FOREIGN KEY (id_user) REFERENCES user_table(personal_id)
);

CREATE TABLE IF NOT EXISTS transfer (
                          id_transfer  BIGINT AUTO_INCREMENT PRIMARY KEY,
                          sender       BIGINT NOT NULL,
                          recipient    BIGINT NOT NULL,
                          title        VARCHAR(255) NOT NULL,
                          date         TIMESTAMP NOT NULL,
                          amount       DECIMAL(10, 2) NOT NULL,
                          id_account   BIGINT NOT NULL,
                          FOREIGN KEY (id_account) REFERENCES account(id_account)
);

CREATE TABLE IF NOT EXISTS loan (
                      id_loan        BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                      amount         DECIMAL(10, 2) NOT NULL,
                      interest_rate  DECIMAL(5, 2) NOT NULL,
                      start_date     TIMESTAMP NOT NULL,
                      end_date       TIMESTAMP NOT NULL,
                      status         ENUM('ACTIVE', 'INACTIVE') NOT NULL,
                      id_account     BIGINT NOT NULL,
                      FOREIGN KEY (id_account) REFERENCES account(id_account)
);

CREATE TABLE IF NOT EXISTS investment (
                            id_investment   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            name            VARCHAR(40) NOT NULL,
                            type            ENUM('FUND', 'GOLD', 'SILVER') NOT NULL,
                            amount          DECIMAL(10, 2) NOT NULL,
                            interest_rate   DECIMAL(5, 2) NOT NULL,
                            start_date      TIMESTAMP NOT NULL,
                            end_date        TIMESTAMP NOT NULL,
                            status          ENUM('ACTIVE', 'INACTIVE') NOT NULL,
                            id_account      BIGINT NOT NULL,
                            FOREIGN KEY (id_account) REFERENCES account(id_account)
);
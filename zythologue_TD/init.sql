CREATE TABLE "User" (
    zyu_id SERIAL PRIMARY KEY,
    zyu_name VARCHAR(30),
    zyu_email VARCHAR(100),
    zyu_password VARCHAR(500),
    zyu_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    zyu_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Brewery (
    zybr_id SERIAL PRIMARY KEY,
    zybr_name VARCHAR(100),
    zybr_country VARCHAR(100),
    zybr_website VARCHAR(10000),
    zybr_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    zybr_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Ingredient (
    zyi_id SERIAL PRIMARY KEY,
    zyi_name VARCHAR(30),
    zyi_type VARCHAR(20),
    zyi_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    zyi_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Category (
    zyc_id SERIAL PRIMARY KEY,
    zyc_name VARCHAR(30),
    zyc_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    zyc_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Beer (
    zyb_id SERIAL PRIMARY KEY,
    zyb_name VARCHAR(100),
    zyb_abv FLOAT,
    zyb_description VARCHAR(10000),
    zyb_brewery INT,
    zyb_category INT,
    zyb_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    zyb_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_brewery FOREIGN KEY (zyb_brewery) REFERENCES Brewery(zybr_id) ON DELETE CASCADE,
    CONSTRAINT fk_category FOREIGN KEY (zyb_category) REFERENCES Category(zyc_id) ON DELETE CASCADE
);

CREATE TABLE BeerIngredient (
    zybi_beer INTEGER,
    zybi_ingredient INTEGER,
    zybi_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    zybi_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_beer FOREIGN KEY (zybi_beer) REFERENCES Beer(zyb_id) ON DELETE CASCADE,
    CONSTRAINT fk_ingredient FOREIGN KEY (zybi_ingredient) REFERENCES Ingredient(zyi_id) ON DELETE CASCADE,
    PRIMARY KEY(zybi_beer, zybi_ingredient)
);

CREATE TABLE Favorite (
    zyf_user INTEGER,
    zyf_beer INTEGER,
    zyf_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    zyf_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (zyf_user) REFERENCES "User"(zyu_id) ON DELETE CASCADE,
    CONSTRAINT fk_beer FOREIGN KEY (zyf_beer) REFERENCES Beer(zyb_id) ON DELETE CASCADE,
    PRIMARY KEY(zyf_user, zyf_beer)
);

CREATE TABLE Review (
    zyr_user INTEGER,
    zyr_beer INTEGER,
    zyr_rating INTEGER,
    zyr_review VARCHAR(10000),
    zyr_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    zyr_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (zyr_user) REFERENCES "User"(zyu_id) ON DELETE CASCADE,
    CONSTRAINT fk_beer FOREIGN KEY (zyr_beer) REFERENCES Beer(zyb_id) ON DELETE CASCADE,
    PRIMARY KEY(zyr_user, zyr_beer)
);

CREATE TABLE Picture (
    zyp_id SERIAL PRIMARY KEY,
    zyp_beer INTEGER,
    zyp_picture VARCHAR(10000),
    zyp_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    zyp_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_beer FOREIGN KEY (zyp_beer) REFERENCES Beer(zyb_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users
(
    id               INT          NOT NULL AUTO_INCREMENT,
    name             VARCHAR(100) NOT NULL,
    email            VARCHAR(100) NOT NULL,
    password         VARCHAR(100) NOT NULL,
    is_admin         TINYINT(1) NOT NULL DEFAULT 0,
    token            VARCHAR(100),
    token_expired_at TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE users_email_unique (email),
    UNIQUE users_token_unique (token)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS products
(
    id          INT            NOT NULL AUTO_INCREMENT,
    sku         VARCHAR(100)   NOT NULL,
    name        VARCHAR(100)   NOT NULL,
    price       DECIMAL(20, 2) NOT NULL DEFAULT 0.00,
    description TEXT,
    image       VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE products_sku_unique (sku),
    UNIQUE products_name_unique (name)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS orders
(
    id           INT            NOT NULL AUTO_INCREMENT,
    user_id      INT            NOT NULL,
    order_number VARCHAR(100)   NOT NULL,
    ordered_at   TIMESTAMP      NOT NULL,
    grand_total  DECIMAL(20, 2) DEFAULT 0.00,
    status       VARCHAR(50)    NOT NULL,
    PRIMARY KEY (id),
    UNIQUE orders_order_number_unique (order_number),
    CONSTRAINT fk_orders_users_user_id
        FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS order_items
(
    id          INT            NOT NULL AUTO_INCREMENT,
    order_id    INT            NOT NULL,
    product_id  INT            NOT NULL,
    name        VARCHAR(100)   NOT NULL,
    sku         VARCHAR(100)   NOT NULL,
    qty_ordered INT            NOT NULL DEFAULT 0,
    price       DECIMAL(20, 2) NOT NULL DEFAULT 0.00,
    raw_total   DECIMAL(20, 2) NOT NULL DEFAULT 0.00,
    PRIMARY KEY (id),
    CONSTRAINT fk_order_items_orders_order_id
        FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_products_product_id
        FOREIGN KEY (product_id) REFERENCES products (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS inventories
(
    id           INT NOT NULL AUTO_INCREMENT,
    sku          VARCHAR(100) NOT NULL,
    qty_total    INT NOT NULL DEFAULT 0,
    qty_reserved INT NOT NULL DEFAULT 0,
    qty_saleable INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT fk_inventories_products_sku
        FOREIGN KEY (sku) REFERENCES products (sku) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS request_logs
(
    id            INT(10) NOT NULL AUTO_INCREMENT,
    created_at    TIMESTAMP NOT NULL,
    ip            VARCHAR(50) NOT NULL,
    request_count INT(10) UNSIGNED NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE INDEX request_logs_ip_unique (ip)
) ENGINE = InnoDB;
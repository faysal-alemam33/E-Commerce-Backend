create table products(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    unit ENUM('KG', 'LITER', 'PIECE') NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    active BOOLEAN DEFAULT TRUE NOT NULL,
    image BLOB
);

create table categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

create table product_category (
    product_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    primary key (product_id, category_id),
    foreign key (product_id) references products(id) ON DELETE CASCADE,
    foreign key (category_id) references categories(id) ON DELETE CASCADE
);
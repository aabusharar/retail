SET SCHEMA PUBLIC;
------ Add User Table ---------
DROP TABLE IF EXISTS user_entity;

CREATE TABLE user_entity (
                             id INT AUTO_INCREMENT  PRIMARY KEY,
                             user_name VARCHAR(250) NOT NULL,
                             first_name VARCHAR(250) NOT NULL,
                             last_name VARCHAR(250) NOT NULL,
                             email VARCHAR(250) NOT NULL,
                             phone_number VARCHAR(250) NOT NULL,
                             type VARCHAR(250) DEFAULT NULL
);

INSERT INTO user_entity (id, user_name, first_name, last_name, email, phone_number, type) VALUES
(1, 'aliko_dangote', 'Aliko', 'Dangote', 'aliko_dangote@test.com', '00962786623200', 'EMPLOYEE'),
(2, 'bill_gates', 'bill', 'gates', 'bill_gates@test.com', '00962786623201', 'AFFILIATE');

------ Add Product Table ---------
DROP TABLE IF EXISTS product_entity;

CREATE TABLE product_entity (
                                id INT AUTO_INCREMENT  PRIMARY KEY,
                                name VARCHAR(250) NOT NULL,
                                price float NOT NULL,
                                product_type VARCHAR(250) NOT NULL
);

INSERT INTO product_entity (id, name, price, product_type) VALUES
(1, 'Meat', 50.0, 'GROCERIES'),
(2, 'Clothes', 100.0, 'SOFT_GOODS');

------ Add order Table ---------
DROP TABLE IF EXISTS order_entity;

CREATE TABLE order_entity (
                              id INT AUTO_INCREMENT  PRIMARY KEY,
                              user_name VARCHAR(250) NOT NULL,
                              address VARCHAR(250) NOT NULL,
                              email VARCHAR(250) NOT NULL,
                              phone_number VARCHAR(250) NOT NULL,
                              total_order_price float NOT NULL,
                              order_Time TIMESTAMP DEFAULT NULL,
                              products json DEFAULT NULL
);

INSERT INTO order_entity (id, user_name, address, email, phone_number, total_order_price, order_Time, products) VALUES
(1, 'aliko_dangote', 'USA', 'aliko_dangote@test.com', '00962786623200', 50.0,  '2021-09-04 18:47:52.69', '{"id": 1, "name": "Meat", "amount": 1 }'),
(2, 'aliko_dangote', 'USA', 'aliko_dangote@test.com', '00962786623200', 100.0, '2014-09-04 18:47:52.69', '{"id": 1, "name": "Meat", "amount": 2 }');

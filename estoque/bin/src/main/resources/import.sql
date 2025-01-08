INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_role (authority) VALUES ('ROLE_OPERATOR');

INSERT INTO tb_company (name, cnpj) VALUES ('Companhia das índias orientais','1234567891011');
INSERT INTO tb_company (name, cnpj) VALUES ('Companhia das índias ocidentias','1110987654321');

INSERT INTO tb_user (name, status, email, password, company_id, first_acess) VALUES ('Alex Brown', true, 'alex.brown@ifpe.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', 1, true);
INSERT INTO tb_user (name, status, email, password, company_id, first_acess) VALUES ('Maria Green', true, 'maria.green@ifpe.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', 1, false);
INSERT INTO tb_user (name, status, email, password, company_id, first_acess) VALUES ('Jhon Peter', true, 'jhon.peter@ifpe.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', 2, false);

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 2);

INSERT INTO tb_product (name, quantity, description, critical_quantity, company_id, unit_value, stock_value) VALUES ('Código Limpo', 1, 'Livro sobre...' ,10 , 1, 10, 100);
INSERT INTO tb_product (name, quantity, description, critical_quantity, company_id, unit_value, stock_value) VALUES ('The Lord of the Rings', 100, 'Livro sobre...', 10 , 1, 45.90, 459);

INSERT INTO tb_product (name, quantity, description, critical_quantity, company_id, unit_value, stock_value) VALUES ('Macbook Pro', 32, 'Notebook...', 7 , 2, 1250.0, 40.000);
INSERT INTO tb_product (name, quantity, description, critical_quantity, company_id, unit_value, stock_value) VALUES ('PC Gamer', 3, 'PC...', 1 , 2, 4.500, 13.500);

INSERT INTO tb_category (name, company_id) VALUES ('Livros', 1);
INSERT INTO tb_category (name, company_id) VALUES ('Eletrônicos', 1);
INSERT INTO tb_category (name, company_id) VALUES ('Eletrônicos', 2);

INSERT INTO tb_product_category (product_id, category_id) VALUES (1, 1);
INSERT INTO tb_product_category (product_id, category_id) VALUES (2, 2);

INSERT INTO tb_product_category (product_id, category_id) VALUES (3, 3);
INSERT INTO tb_product_category (product_id, category_id) VALUES (4, 3);
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_role (authority) VALUES ('ROLE_OPERATOR');

INSERT INTO tb_company (name, cnpj) VALUES ('Companhia das índias orientais','1234567891011');
INSERT INTO tb_company (name, cnpj) VALUES ('Companhia das índias ocidentias','1110987654321');

INSERT INTO tb_user (name, status, email, password, company_id) VALUES ('Alex Brown', true, 'alex.brown@ifpe.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', 1);
INSERT INTO tb_user (name, status, email, password, company_id) VALUES ('Maria Green', true, 'maria.green@ifpe.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', 1);
INSERT INTO tb_user (name, status, email, password, company_id) VALUES ('Jhon Peter', true, 'jhon.peter@ifpe.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', 2);

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 2);

INSERT INTO tb_product (name, quantity, critical_quantity, company_id, unit_value, stock_value) VALUES ('Produto 1', 1, 10 , 1, 1, 1);
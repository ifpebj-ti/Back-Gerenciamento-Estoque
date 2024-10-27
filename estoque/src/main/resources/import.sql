INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');
INSERT INTO tb_role (authority) VALUES ('ROLE_OPERATOR');

INSERT INTO tb_company (id, name, cnpj) VALUES ('e3b9deaf-5e5f-424d-9063-cb32e1e7a6f3', 'Companhia das índias orientais','1234567891011');
INSERT INTO tb_company (id, name, cnpj) VALUES ('e044a18b-14fb-475f-88fb-8094120bcc48', 'Companhia das índias ocidentias','1110987654321');

INSERT INTO tb_user (id, name, status, email, password, company_id) VALUES ('e3b9deaf-5e5f-424d-9063-cb32e1e7a6f4', 'Alex Brown', true, 'alex.brown@ifpe.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', 'e3b9deaf-5e5f-424d-9063-cb32e1e7a6f3');
INSERT INTO tb_user (id, name, status, email, password, company_id) VALUES ('e044a18b-14fb-475f-88fb-8094120bcc47', 'Maria Green', true, 'maria.green@ifpe.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', 'e3b9deaf-5e5f-424d-9063-cb32e1e7a6f3');
INSERT INTO tb_user (id, name, status, email, password, company_id) VALUES ('6f1c8e4a-c9ad-4c3d-87e0-acd6d7de64a0', 'Jhon Peter', true, 'jhon.peter@ifpe.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', 'e044a18b-14fb-475f-88fb-8094120bcc48');

INSERT INTO tb_user_role (user_id, role_id) VALUES ('e3b9deaf-5e5f-424d-9063-cb32e1e7a6f4', 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES ('e044a18b-14fb-475f-88fb-8094120bcc47', 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES ('6f1c8e4a-c9ad-4c3d-87e0-acd6d7de64a0', 2);
CREATE TABLE role
(
    id_role BIGINT NOT NULL,
    nome    VARCHAR(255),
    CONSTRAINT pk_role PRIMARY KEY (id_role)
);

CREATE TABLE roles_usuarios
(
    id_role    BIGINT NOT NULL,
    id_usuario BIGINT NOT NULL,
    CONSTRAINT pk_roles_usuarios PRIMARY KEY (id_role, id_usuario)
);

CREATE TABLE tb_empresa
(
    id_empresa         BIGINT       NOT NULL,
    usuario_id_usuario BIGINT,
    nome               VARCHAR(255),
    cnpj               VARCHAR(255) NOT NULL,
    telefone           VARCHAR(255) NOT NULL,
    CONSTRAINT pk_tb_empresa PRIMARY KEY (id_empresa)
);

CREATE TABLE tb_usuario
(
    id_usuario BIGINT       NOT NULL,
    nome       VARCHAR(50)  NOT NULL,
    sobrenome  VARCHAR(50)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    senha      VARCHAR(255) NOT NULL,
    created_at BIGINT       NOT NULL,
    CONSTRAINT pk_tb_usuario PRIMARY KEY (id_usuario)
);

ALTER TABLE tb_empresa
    ADD CONSTRAINT uc_tb_empresa_cnpj UNIQUE (cnpj);

ALTER TABLE tb_usuario
    ADD CONSTRAINT uc_tb_usuario_email UNIQUE (email);

ALTER TABLE tb_empresa
    ADD CONSTRAINT FK_TB_EMPRESA_ON_USUARIO_IDUSUARIO FOREIGN KEY (usuario_id_usuario) REFERENCES tb_usuario (id_usuario);

ALTER TABLE roles_usuarios
    ADD CONSTRAINT fk_rolusu_on_role FOREIGN KEY (id_role) REFERENCES role (id_role);

ALTER TABLE roles_usuarios
    ADD CONSTRAINT fk_rolusu_on_usuario FOREIGN KEY (id_usuario) REFERENCES tb_usuario (id_usuario);
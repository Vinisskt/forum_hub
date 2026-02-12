CREATE TABLE topicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(255) NOT NULL,
    curso VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    data DATETIME NOT NULL,
    status VARCHAR(255) NOT NULL,
    id_usuario BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_usuario)
        REFERENCES usuarios (id)
);


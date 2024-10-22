CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome_usuario VARCHAR(50) NOT NULL,
    hash_senha VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    data_criado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_login TIMESTAMP NULL DEFAULT NULL
);

CREATE TABLE transacoes (
    id_transacao INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    valor DECIMAL(10, 2) NOT NULL,
    tipo_transacao ENUM('entrada', 'saida') NOT NULL,
    data_transacao DATE NOT NULL,
    data_criado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE saldos (
    id_saldos INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    saldo_atual DECIMAL(10, 2) NOT NULL DEFAULT 0,
    ultima_modificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);
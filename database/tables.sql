-- Criar o banco de dados se não existir
CREATE DATABASE IF NOT EXISTS livro_caixa;

-- Usar o banco de dados criado
USE livro_caixa;

-- Criar a tabela de usuários
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome_usuario VARCHAR(50) NOT NULL,
    hash_senha VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    data_criado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    renda_mensal INT,
    ultimo_login TIMESTAMP NULL DEFAULT NULL
);

-- Criar a tabela de transações
CREATE TABLE IF NOT EXISTS transacoes (
    id_transacao INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    valor DECIMAL(10, 2) NOT NULL,
    tipo_transacao ENUM('entrada', 'saida') NOT NULL,
    data_transacao DATE NOT NULL,
    data_criado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- Criar a tabela de saldos
CREATE TABLE IF NOT EXISTS saldos (
    id_saldos INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    saldo_atual DECIMAL(10, 2) NOT NULL DEFAULT 0,
    ultima_modificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

DELIMITER $$

CREATE TRIGGER atualiza_saldo
AFTER INSERT ON transacoes
FOR EACH ROW
BEGIN
    -- Verifica se já existe um saldo para o usuário
    IF EXISTS (SELECT 1 FROM saldos WHERE id_usuario = NEW.id_usuario) THEN
        -- Atualiza o saldo existente
        UPDATE saldos
        SET saldo_atual = saldo_atual + 
            CASE 
                WHEN NEW.tipo_transacao = 'entrada' THEN NEW.valor
                ELSE -NEW.valor
            END,
            ultima_modificacao = CURRENT_TIMESTAMP
        WHERE id_usuario = NEW.id_usuario;
    ELSE
        -- Insere um novo registro de saldo
        INSERT INTO saldos (id_usuario, saldo_atual, ultima_modificacao)
        VALUES (
            NEW.id_usuario,
            CASE 
                WHEN NEW.tipo_transacao = 'entrada' THEN NEW.valor
                ELSE -NEW.valor
            END,
            CURRENT_TIMESTAMP
        );
    END IF;
END$$

DELIMITER ;
INSERT INTO usuarios (nome_usuario, hash_senha, email, renda_mensal)
VALUES
('João Silva', SHA2('senha123', 256), 'joao.silva@example.com', 3500),
('Maria Oliveira', SHA2('senha456', 256), 'maria.oliveira@example.com', 4500),
('Carlos Santos', SHA2('senha789', 256), 'carlos.santos@example.com', 2800),
('Ana Costa', SHA2('minhasenha', 256), 'ana.costa@example.com', 5000),
('Pedro Martins', SHA2('pass1234', 256), 'pedro.martins@example.com', 3200),
('Julia Souza', SHA2('senhafacil', 256), 'julia.souza@example.com', 4200),
('Marcos Almeida', SHA2('123senha', 256), 'marcos.almeida@example.com', 3100),
('Clara Ribeiro', SHA2('testesenha', 256), 'clara.ribeiro@example.com', 3700),
('Lucas Pereira', SHA2('lucas123', 256), 'lucas.pereira@example.com', 2900),
('Fernanda Lima', SHA2('fernanda789', 256), 'fernanda.lima@example.com', 6100);

-- Inserir transações fictícias para teste
INSERT INTO transacoes (id_usuario, valor, tipo_transacao, data_transacao)
VALUES
(1, 500.00, 'entrada', '2024-11-01'),
(1, 100.00, 'saida', '2024-11-05'),
(2, 2000.00, 'entrada', '2024-11-02'),
(2, 300.00, 'saida', '2024-11-06'),
(3, 1500.00, 'entrada', '2024-11-03'),
(3, 500.00, 'saida', '2024-11-08'),
(4, 800.00, 'entrada', '2024-11-04'),
(4, 200.00, 'saida', '2024-11-09'),
(5, 1200.00, 'entrada', '2024-11-05'),
(5, 300.00, 'saida', '2024-11-10');
-- sql rodrigo
DROP SCHEMA IF EXISTS `paresp`;
CREATE SCHEMA IF NOT EXISTS `paresp` DEFAULT CHARACTER SET utf8;
USE `paresp`;

-- Necessário para que o comando UPDATE (se usado) funcione sem restrições
SET SQL_SAFE_UPDATES = 0;

-- -----------------------------------------------------
-- Tabela `administrador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `administrador` (
  `pk_cod_admin` INT(4) NOT NULL AUTO_INCREMENT,
  `senha` VARCHAR(60) NOT NULL, -- Tamanho para hash BCrypt
  `telefone` CHAR(11) NULL,
  `nome` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`pk_cod_admin`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Tabela `escola`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `escola` (
  `pk_cod_escola` INT(6) NOT NULL,
  `nome` VARCHAR(45) NOT NULL,
  `serie` VARCHAR(3) NOT NULL,
  PRIMARY KEY (`pk_cod_escola`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Tabela `aluno` (INCLUI data_vacinacao)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `aluno` (
  `pk_cod_pessoa` INT(6) NOT NULL AUTO_INCREMENT,
  `fk_cod_admin` INT(4) NOT NULL,
  `fk_cod_escola` INT(6) NOT NULL,
  `cpf` CHAR(11) NOT NULL,
  `data_acolhimento` DATE NOT NULL,
  `form_acesso` VARCHAR(15) NOT NULL,
  `vacinacao` TINYINT(1) NOT NULL,
  `data_vacinacao` DATE NULL, -- NOVA COLUNA: Data específica da última vacina
  `termo_imagem` TINYINT(1) NOT NULL,
  `turno` VARCHAR(6) NOT NULL,
  `transporte` VARCHAR(45) NULL,
  `data_nasc` DATE NOT NULL,
  `proj_ferias` TINYINT(1) NOT NULL,
  `nome` VARCHAR(45) NOT NULL,
  `sexo` CHAR(1) NOT NULL,
  `tamanho_vest` VARCHAR(3) NOT NULL,
  `tamanho_calc` INT(2) NOT NULL,
  `turma` VARCHAR(3) NULL,
  `num_nis` CHAR(11) NULL,
  `med_paresp` VARCHAR(45) NULL,
  `alergias` VARCHAR(45) NULL,
  `observacoes` VARCHAR(45) NULL,
  `intervencoes` VARCHAR(45) NULL,
  `evolucoes` VARCHAR(45) NULL,
  `status` INT(1) NOT NULL,
  PRIMARY KEY (`pk_cod_pessoa`),
  UNIQUE INDEX `cpf_UNIQUE` (`cpf` ASC),
  CONSTRAINT `fk_aluno_administrador`
    FOREIGN KEY (`fk_cod_admin`)
    REFERENCES `administrador` (`pk_cod_admin`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_aluno_escola`
    FOREIGN KEY (`fk_cod_escola`)
    REFERENCES `escola` (`pk_cod_escola`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Tabela `familia` (INCLUI anotacoes)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `familia` (
  `pk_cod_familia` INT(6) NOT NULL AUTO_INCREMENT,
  `fk_cod_pessoa` INT(6) NOT NULL,
  `qnt_integrantes_fam` INT(2) NOT NULL,
  `renda_familiar_total` DECIMAL(8,2) NOT NULL,
  `bolsa_familia` DECIMAL(8,2) NULL,
  `endereco_familia` VARCHAR(45) NOT NULL,
  `bairro_familia` VARCHAR(20) NOT NULL,
  `residencia` VARCHAR(12) NOT NULL,
  `valor_aluguel` DECIMAL(8,2) NULL,
  `telefone_familia` CHAR(11) NOT NULL,
  `anotacoes` VARCHAR(255) NULL, -- NOVA COLUNA: Anotações sobre a família
  PRIMARY KEY (`pk_cod_familia`),
  CONSTRAINT `fk_familia_aluno`
    FOREIGN KEY (`fk_cod_pessoa`)
    REFERENCES `aluno` (`pk_cod_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Tabela `integrante_familia` (INCLUI anotacoes)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `integrante_familia` (
  `pk_cod_integrante` INT(4) NOT NULL AUTO_INCREMENT,
  `fk_cod_familia` INT(6) NOT NULL,
  `nome` VARCHAR(45) NOT NULL,
  `cpf` CHAR(11) NOT NULL,
  `parentesco` VARCHAR(45) NULL,
  `vinculo_afetivo` TINYINT(1) NOT NULL,
  `ocupacao` VARCHAR(45) NULL,
  `endereco` VARCHAR(45) NULL,
  `telefone` CHAR(11) NULL,
  `resp_legal` TINYINT(1) NOT NULL,
  `pessoa_autorizada` TINYINT(1) NOT NULL,
  `anotacoes` VARCHAR(255) NULL, -- NOVA COLUNA: Anotações sobre o integrante
  PRIMARY KEY (`pk_cod_integrante`),
  CONSTRAINT `fk_integrante_familia_familia`
    FOREIGN KEY (`fk_cod_familia`)
    REFERENCES `familia` (`pk_cod_familia`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- DADOS INICIAIS (Administrador e Escolas)
-- -----------------------------------------------------
INSERT INTO administrador (pk_cod_admin, senha, telefone, nome)
VALUES (1, '$2a$10$8upSGyqoGbOXkRdgIX2QoOxmyK/u56Dq66OdGfmmYcQ0H9uODrw6e', '51999999999', 'Admin Principal'); -- Senha: 12345678

INSERT INTO escola (pk_cod_escola, nome, serie)
VALUES 
(1, 'Escola Municipal Modelo', '5A'),
(2, 'Escola Estadual Progresso', '3C');

-- -----------------------------------------------------
-- DADOS DE TESTE (Aluno 1: Gustavo - Cadastro Básico)
-- -----------------------------------------------------
INSERT INTO aluno (
  fk_cod_admin, fk_cod_escola, cpf, data_acolhimento, form_acesso, vacinacao, data_vacinacao,
  termo_imagem, turno, transporte, data_nasc, proj_ferias, nome, sexo,
  tamanho_vest, tamanho_calc, turma, num_nis, med_paresp, alergias, observacoes,
  intervencoes, evolucoes, status
) VALUES (
  1, 1, '98765432100', '2025-09-05', 'Cadastro', 1, '2025-03-15', -- vacinacao 1 e data
  1, 'Manhã', 'Ônibus Escolar', '2010-05-15', 1, 'Gustavo Silva', 'M',
  'M', 40, '5A', '12345678910', NULL, 'Nenhuma', 'Filho único.',
  'Encaminhado para psicólogo.', 'Começou a interagir mais.', 1
);

-- -----------------------------------------------------
-- DADOS DE TESTE COMPLETOS (Aluno 2: Isabella - Para teste de Família/Saúde)
-- -----------------------------------------------------
INSERT INTO aluno (
  fk_cod_admin, fk_cod_escola, cpf, data_acolhimento, form_acesso, vacinacao, data_vacinacao,
  termo_imagem, turno, transporte, data_nasc, proj_ferias, nome, sexo,
  tamanho_vest, tamanho_calc, turma, num_nis, med_paresp, alergias, observacoes,
  intervencoes, evolucoes, status
) VALUES (
  1, 2, '50000000000', '2025-01-20', 'Busca Ativa', 1, '2025-10-01',
  1, 'Tarde', 'Van', '2012-11-10', 1, 'Isabella Teste', 'F',
  'G', 36, '6A', '50000000000', 'Ibuprofeno', 'Leite', 'Aluna muito tímida.',
  'Inclusão em grupo de leitura.', 'Melhorou comunicação.', 1
);

-- -----------------------------------------------------
-- DADOS DE TESTE COMPLETOS (Família 1 - Vinculada a Isabella, pk_cod_pessoa=2)
-- -----------------------------------------------------
INSERT INTO familia (
    fk_cod_pessoa, qnt_integrantes_fam, renda_familiar_total, bolsa_familia, 
    endereco_familia, bairro_familia, residencia, valor_aluguel, telefone_familia, anotacoes
) VALUES (
    2, 4, 2500.50, 300.00, 
    'Rua Teste, 100', 'Centro', 'Alugada', 800.00, '51988887777', 'Família enfrenta dificuldades financeiras devido à pandemia.'
);

-- -----------------------------------------------------
-- DADOS DE TESTE COMPLETOS (Integrante 1 - Mãe de Isabella, vinculada a família pk_cod_familia=1)
-- -----------------------------------------------------
INSERT INTO integrante_familia (
  fk_cod_familia, nome, cpf, parentesco, vinculo_afetivo, 
  ocupacao, endereco, telefone, resp_legal, pessoa_autorizada, anotacoes
) VALUES (
  1, 'Maria Mãe', '55555555555', 'Mãe', 1, 
  'Doméstica', 'Mesmo da Familia', '51988887777', 1, 1, 'Responsável principal pela aluna.'
);

ALTER TABLE aluno 
MODIFY COLUMN form_acesso VARCHAR(25) NOT NULL;

-- --- ADMIN TESTE (Senha em Texto Puro para Ambientes Sem BCrypt Estável) ---
-- IMPORTANTE: No teu AdministradorService atual, a lógica espera o HASH.
-- Se o teu login ainda estiver usando a comparação de String para "123", use o nome "Usuario Teste".
INSERT INTO administrador (pk_cod_admin, senha, telefone, nome)
VALUES (2, '123', '51987654321', 'Usuario Teste'); 

-- --- DADOS DE TESTE ADICIONAIS (20 ALUNOS) ---
-- Usaremos fk_cod_admin=1 e fk_cod_escola=1 para a maioria.

INSERT INTO aluno (
    fk_cod_admin, fk_cod_escola, cpf, data_acolhimento, form_acesso, vacinacao, data_vacinacao,
    termo_imagem, turno, transporte, data_nasc, proj_ferias, nome, sexo,
    tamanho_vest, tamanho_calc, turma, num_nis, med_paresp, alergias, observacoes,
    intervencoes, evolucoes, status
) VALUES 
-- 1. Aluno com dados incompletos (para filtro)
(1, 1, '40000000001', '2025-01-01', 'Demanda Espontânea', 0, NULL,
 1, 'Manhã', 'A pé', '2014-05-01', 0, 'Ana Paula Gomes', 'F',
 'M', 35, '2B', '40000000001', NULL, 'Pólen', NULL, NULL, NULL, 1),
-- 2. Aluno com atraso vacinal
(1, 2, '40000000002', '2025-03-01', 'Encaminhamento', 0, '2022-01-01',
 1, 'Tarde', 'Van', '2016-08-10', 1, 'Bruno Mendes', 'M',
 'P', 32, '3C', '40000000002', 'Dipirona', NULL, 'Aluno reservado.', NULL, NULL, 1),
-- 3. Aluno com alergia
(1, 1, '40000000003', '2025-02-15', 'Busca Ativa', 1, '2025-06-20',
 0, 'Manhã', 'Ônibus Escolar', '2013-03-22', 0, 'Carla de Souza', 'F',
 'G', 38, '5A', '40000000003', NULL, 'Lactose', NULL, NULL, NULL, 1),
-- 4. Aluno com NIS
(1, 2, '40000000004', '2025-04-10', 'Cadastro', 1, '2024-11-20',
 1, 'Tarde', 'Carona', '2015-12-05', 1, 'Daniela Ferreira', 'F',
 'P', 34, '2B', '40000000004', 'Ritalina', NULL, 'Observar interação.', 'Plano pedagógico.', 'Boas notas.', 1),
-- 5. Aluno inativado (status=0)
(1, 1, '40000000005', '2025-05-20', 'Demanda Espontânea', 1, '2025-01-15',
 0, 'Manhã', 'A pé', '2017-09-19', 0, 'Eduardo Henrique', 'M',
 'M', 36, '1A', '40000000005', NULL, NULL, NULL, NULL, NULL, 0),
-- 6. Aluno - Projeto Férias
(1, 1, '40000000006', '2025-06-05', 'Encaminhamento', 1, '2024-05-10',
 1, 'Tarde', 'Van', '2016-01-28', 1, 'Fabiana Lima', 'F',
 'M', 35, '3C', '40000000006', NULL, NULL, NULL, NULL, NULL, 1),
-- 7. Aluno - Grande
(1, 2, '40000000007', '2025-07-10', 'Busca Ativa', 1, '2025-03-11',
 1, 'Manhã', 'Ônibus Escolar', '2012-07-07', 1, 'Gabriel Souza', 'M',
 'GG', 42, '4B', '40000000007', NULL, NULL, 'Ótimo aluno.', NULL, NULL, 1),
-- 8. Aluno - Pequeno
(1, 1, '40000000008', '2025-08-01', 'Cadastro', 0, NULL,
 0, 'Tarde', 'Carona', '2018-11-14', 0, 'Heloisa Rocha', 'F',
 'P', 30, '1C', '40000000008', NULL, NULL, NULL, NULL, NULL, 1),
-- 9. Aluno com intervenção
(1, 1, '40000000009', '2025-09-15', 'Demanda Espontânea', 1, '2024-08-25',
 1, 'Manhã', 'A pé', '2014-02-09', 1, 'Igor Castro', 'M',
 'G', 38, '5A', '40000000009', NULL, NULL, NULL, 'Acompanhamento social.', 'Positiva.', 1),
-- 10. Aluno com evolução
(1, 2, '40000000010', '2025-10-25', 'Encaminhamento', 1, '2025-04-16',
 1, 'Tarde', 'Van', '2016-05-29', 0, 'Jéssica Lima', 'F',
 'M', 34, '2B', '40000000010', NULL, NULL, NULL, NULL, 'Necessita apoio.', 1),
 
-- 11-20 (Dados Variados)
(1, 1, '40000000011', '2025-01-01', 'Busca Ativa', 1, '2025-09-01', 1, 'Manhã', 'Van', '2013-09-01', 1, 'Kauã Henrique', 'M', 'P', 32, '4A', '40000000011', NULL, NULL, NULL, NULL, NULL, 1),
(1, 2, '40000000012', '2025-03-01', 'Cadastro', 0, '2023-01-01', 0, 'Tarde', 'A pé', '2017-02-01', 0, 'Lara Beatriz', 'F', 'G', 36, '1C', '40000000012', NULL, 'Gatos', NULL, NULL, NULL, 1),
(1, 1, '40000000013', '2025-02-15', 'Demanda Espontânea', 1, '2024-10-20', 1, 'Manhã', 'Carro', '2015-08-15', 1, 'Marcelo Rocha', 'M', 'M', 38, '3C', '40000000013', NULL, NULL, 'Aluno alegre.', NULL, NULL, 1),
(1, 2, '40000000014', '2025-04-10', 'Encaminhamento', 1, '2025-07-01', 0, 'Tarde', 'Van', '2016-12-12', 0, 'Natália Costa', 'F', 'P', 34, '2B', '40000000014', 'Fluoxetina', NULL, NULL, 'Sessões de reforço.', NULL, 1),
(1, 1, '40000000015', '2025-05-20', 'Busca Ativa', 0, NULL, 1, 'Manhã', 'Ônibus Escolar', '2018-04-04', 1, 'Otávio Martins', 'M', 'G', 40, '5B', '40000000015', NULL, NULL, NULL, NULL, NULL, 1),
(1, 1, '40000000016', '2025-06-05', 'Cadastro', 1, '2024-03-10', 1, 'Tarde', 'Carona', '2013-11-25', 0, 'Priscila Alves', 'F', 'M', 35, '3A', '40000000016', NULL, NULL, NULL, NULL, NULL, 1),
(1, 2, '40000000017', '2025-07-10', 'Demanda Espontânea', 1, '2025-01-05', 0, 'Manhã', 'A pé', '2017-01-01', 1, 'Quésia Miranda', 'F', 'P', 32, '1A', '40000000017', NULL, NULL, NULL, NULL, NULL, 1),
(1, 1, '40000000018', '2025-08-01', 'Encaminhamento', 0, NULL, 1, 'Tarde', 'Van', '2014-06-18', 0, 'Renan Santos', 'M', 'M', 37, '4C', '40000000018', NULL, NULL, NULL, 'Reunião familiar.', NULL, 1),
(1, 1, '40000000019', '2025-09-15', 'Busca Ativa', 1, '2025-04-04', 0, 'Manhã', 'Carro', '2015-03-10', 1, 'Sofia Teixeira', 'F', 'G', 39, '5A', '40000000019', NULL, NULL, NULL, NULL, NULL, 1),
(1, 2, '40000000020', '2025-10-25', 'Cadastro', 1, '2024-09-09', 1, 'Tarde', 'Ônibus Escolar', '2016-10-10', 0, 'Thiago Cruz', 'M', 'M', 35, '2C', '40000000020', NULL, NULL, NULL, NULL, NULL, 1);
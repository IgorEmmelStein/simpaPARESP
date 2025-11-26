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
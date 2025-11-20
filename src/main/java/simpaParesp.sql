-- sql rodrigo
DROP SCHEMA IF EXISTS `paresp`;
CREATE SCHEMA IF NOT EXISTS `paresp` DEFAULT CHARACTER SET utf8;
USE `paresp`;

CREATE TABLE IF NOT EXISTS `administrador` (
  `pk_cod_admin` INT(4) NOT NULL AUTO_INCREMENT,
  `cpf` CHAR(11) NOT NULL,
  `senha` VARCHAR(20) NOT NULL,
  `telefone` CHAR(11) NULL,
  `nome` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`pk_cod_admin`),
  UNIQUE INDEX `cpf_UNIQUE` (`cpf` ASC)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `escola` (
  `pk_cod_escola` INT(6) NOT NULL,
  `nome` VARCHAR(45) NOT NULL,
  `serie` VARCHAR(3) NOT NULL,
  PRIMARY KEY (`pk_cod_escola`)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `aluno` (
  `pk_cod_pessoa` INT(6) NOT NULL AUTO_INCREMENT,
  `fk_cod_admin` INT(4) NOT NULL,
  `fk_cod_escola` INT(6) NOT NULL,
  `cpf` CHAR(11) NOT NULL,
  `data_acolhimento` DATE NOT NULL,
  `form_acesso` VARCHAR(15) NOT NULL,
  `vacinacao` TINYINT(1) NOT NULL,
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
  PRIMARY KEY (`pk_cod_familia`),
  CONSTRAINT `fk_familia_aluno`
    FOREIGN KEY (`fk_cod_pessoa`)
    REFERENCES `aluno` (`pk_cod_pessoa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

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
  PRIMARY KEY (`pk_cod_integrante`),
  CONSTRAINT `fk_integrante_familia_familia`
    FOREIGN KEY (`fk_cod_familia`)
    REFERENCES `familia` (`pk_cod_familia`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;



INSERT INTO administrador (cpf, senha, telefone, nome)
VALUES ('12345678901', '12345678', '51999999999', 'Admin Principal');

INSERT INTO escola (pk_cod_escola, nome, serie)
VALUES (1, 'Escola Municipal Modelo', '5A');

INSERT INTO aluno (
  fk_cod_admin, fk_cod_escola, cpf, data_acolhimento, form_acesso, vacinacao,
  termo_imagem, turno, transporte, data_nasc, proj_ferias, nome, sexo,
  tamanho_vest, tamanho_calc, turma, num_nis, med_paresp, alergias, observacoes,
  intervencoes, evolucoes, status
) VALUES (
  1, 1, '98765432100', '2025-09-05', 'Cadastro', 1, 1, 'Manhã',
  'Ônibus Escolar', '2010-05-15', 1, 'Gustavo', 'M', 'M', 40, '5A',
  '12345678910', NULL, NULL, NULL, NULL, NULL, 1
);

-- Garante que o ID da escola e do admin de teste existam (baseado no teu script)
-- O ID do Admin Principal é 1.
-- O ID da Escola Municipal Modelo é 1.

select * from aluno;

INSERT INTO aluno (
    fk_cod_admin, fk_cod_escola, cpf, data_acolhimento, form_acesso, vacinacao,
    termo_imagem, turno, transporte, data_nasc, proj_ferias, nome, sexo,
    tamanho_vest, tamanho_calc, turma, num_nis, med_paresp, alergias, observacoes,
    intervencoes, evolucoes, status
) VALUES 
-- ALUNOS FEMININOS (F)
(1, 1, '10000000001', '2025-01-10', 'Encaminhamento', 1, 1, 'Manhã', 'Pé', '2015-08-20', 0, 'Alice Costa', 'F', 'P', 32, '3A', '10000000001', NULL, 'Pólen', NULL, NULL, NULL, 1),
(1, 1, '10000000002', '2025-02-15', 'Busca Ativa', 0, 1, 'Tarde', 'Van Escolar', '2016-03-05', 1, 'Beatriz Silva', 'F', 'M', 34, '2B', '10000000002', 'Ritalina', NULL, NULL, NULL, NULL, 1),
(1, 1, '10000000003', '2025-03-20', 'Cadastro', 1, 0, 'Manhã', 'Ônibus Escolar', '2017-11-12', 0, 'Carolina Ferreira', 'F', 'G', 36, '4A', '10000000003', NULL, NULL, NULL, NULL, NULL, 1),
(1, 1, '10000000004', '2025-04-25', 'Encaminhamento', 1, 1, 'Tarde', 'Pé', '2014-06-28', 1, 'Daniela Gomes', 'F', 'P', 30, '1C', '10000000004', NULL, 'Amendoim', NULL, NULL, NULL, 1),
(1, 1, '10000000005', '2025-05-30', 'Busca Ativa', 0, 0, 'Manhã', 'Van Escolar', '2013-02-14', 0, 'Eduarda Almeida', 'F', 'PP', 28, '5B', '10000000005', NULL, NULL, 'Asma leve', NULL, NULL, 1),
(1, 1, '10000000006', '2025-06-05', 'Cadastro', 1, 1, 'Tarde', 'Pé', '2015-10-01', 1, 'Fernanda Lima', 'F', 'M', 33, '3B', '10000000006', NULL, NULL, NULL, NULL, NULL, 1),
(1, 1, '10000000007', '2025-07-10', 'Encaminhamento', 0, 1, 'Manhã', 'Ônibus Escolar', '2018-04-18', 0, 'Gabriela Pereira', 'F', 'P', 30, '1A', '10000000007', NULL, 'Frutos do mar', NULL, NULL, NULL, 1),
(1, 1, '10000000008', '2025-08-15', 'Busca Ativa', 1, 0, 'Tarde', 'Van Escolar', '2016-12-25', 1, 'Helena Rodrigues', 'F', 'G', 35, '2C', '10000000008', 'Sertralina', NULL, NULL, NULL, NULL, 1),
(1, 1, '10000000009', '2025-09-20', 'Cadastro', 1, 1, 'Manhã', 'Pé', '2017-09-09', 0, 'Isabela Santos', 'F', 'PP', 29, '4B', '10000000009', NULL, NULL, NULL, NULL, NULL, 1),
(1, 1, '10000000010', '2025-10-25', 'Encaminhamento', 0, 0, 'Tarde', 'Ônibus Escolar', '2014-01-30', 1, 'Júlia Oliveira', 'F', 'M', 36, '5A', '10000000010', NULL, 'Látex', NULL, NULL, NULL, 1),

-- ALUNOS MASCULINOS (M)
(1, 1, '20000000001', '2025-01-10', 'Cadastro', 1, 1, 'Manhã', 'Pé', '2015-05-15', 1, 'Marcos Junior', 'M', 'M', 38, '3C', '20000000001', NULL, NULL, NULL, NULL, NULL, 1),
(1, 1, '20000000002', '2025-02-15', 'Busca Ativa', 0, 1, 'Tarde', 'Van Escolar', '2016-09-01', 0, 'Pedro Henrique', 'M', 'G', 40, '2A', '20000000002', NULL, 'Picada de inseto', NULL, NULL, NULL, 1),
(1, 1, '20000000003', '2025-03-20', 'Encaminhamento', 1, 0, 'Manhã', 'Ônibus Escolar', '2017-07-07', 1, 'Rafael Nogueira', 'M', 'P', 35, '4C', '20000000003', 'Paracetamol', NULL, NULL, NULL, NULL, 1),
(1, 1, '20000000004', '2025-04-25', 'Cadastro', 0, 1, 'Tarde', 'Pé', '2014-04-20', 0, 'Thiago Alves', 'M', 'GG', 42, '1B', '20000000004', NULL, NULL, 'TDAH', NULL, NULL, 1),
(1, 1, '20000000005', '2025-05-30', 'Busca Ativa', 1, 0, 'Manhã', 'Van Escolar', '2013-10-10', 1, 'Victor Hugo', 'M', 'M', 39, '5C', '20000000005', NULL, NULL, NULL, NULL, NULL, 1),
(1, 1, '20000000006', '2025-06-05', 'Encaminhamento', 1, 1, 'Tarde', 'Ônibus Escolar', '2015-12-05', 0, 'William Rocha', 'M', 'P', 34, '3A', '20000000006', NULL, 'Gatos', NULL, NULL, NULL, 1),
(1, 1, '20000000007', '2025-07-10', 'Cadastro', 0, 1, 'Manhã', 'Pé', '2018-01-22', 1, 'Xavier Souza', 'M', 'PP', 29, '1A', '20000000007', NULL, NULL, NULL, NULL, NULL, 1),
(1, 1, '20000000008', '2025-08-15', 'Busca Ativa', 1, 0, 'Tarde', 'Van Escolar', '2016-06-06', 0, 'Yuri Mendes', 'M', 'G', 38, '2B', '20000000008', 'Dipirona', NULL, NULL, NULL, NULL, 1),
(1, 1, '20000000009', '2025-09-20', 'Encaminhamento', 0, 1, 'Manhã', 'Ônibus Escolar', '2017-03-17', 1, 'Zeca Fonseca', 'M', 'M', 37, '4A', '20000000009', NULL, 'Glúten', NULL, NULL, NULL, 1),
(1, 1, '20000000010', '2025-10-25', 'Cadastro', 1, 1, 'Tarde', 'Pé', '2014-08-08', 0, 'Leonardo Paz', 'M', 'GG', 41, '5B', '20000000010', NULL, NULL, NULL, NULL, NULL, 1);
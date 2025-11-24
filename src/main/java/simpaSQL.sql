-- sql rodrigo
-- DROP SCHEMA IF EXISTS `paresp`;
CREATE SCHEMA IF NOT EXISTS `paresp` DEFAULT CHARACTER SET utf8;
USE `paresp`;

CREATE TABLE IF NOT EXISTS `administrador` ( -- foi tirado o cpf pois vimos que não era necessário.
  `pk_cod_admin` INT(4) NOT NULL AUTO_INCREMENT,
  `senha` VARCHAR(60) NOT NULL, -- troquei para 60 por conta da encriptação, que necessita que seja 60 caracteres.
  `telefone` CHAR(11) NULL,
  `nome` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`pk_cod_admin`)
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

select nome from escola;

INSERT INTO administrador (senha, telefone, nome)
VALUES ('$2a$10$8upSGyqoGbOXkRdgIX2QoOxmyK/u56Dq66OdGfmmYcQ0H9uODrw6e', '51999999999', 'Admin Principal');-- agora a senha está criptografada. significa 12345678

UPDATE administrador 
SET senha = '$2a$10$8upSGyqoGbOXkRdgIX2QoOxmyK/u56Dq66OdGfmmYcQ0H9uODrw6e' 
WHERE nome = 'Admin Principal';

SET SQL_SAFE_UPDATES = 0;

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
-- -----------------------------------------------------
-- Schema simpaParesp
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS simpaParesp DEFAULT CHARACTER SET utf8;
USE simpaParesp;

-- -----------------------------------------------------
-- Table administrador
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS administrador (
  pk_cod_admin INT NOT NULL AUTO_INCREMENT,
  cpf CHAR(11) NOT NULL,
  senha VARCHAR(8) NOT NULL,
  telefone CHAR(11) NULL,
  nome VARCHAR(45) NOT NULL,
  PRIMARY KEY (pk_cod_admin)
) ENGINE = InnoDB;

CREATE UNIQUE INDEX pk_cod_admin_UNIQUE ON administrador (pk_cod_admin ASC);
CREATE UNIQUE INDEX cpf_UNIQUE ON administrador (cpf ASC);
CREATE UNIQUE INDEX senha_UNIQUE ON administrador (senha ASC);
CREATE UNIQUE INDEX telefone_UNIQUE ON administrador (telefone ASC);

-- -----------------------------------------------------
-- Table escola
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS escola (
  pk_cod_escola INT NOT NULL,
  nome VARCHAR(45) NOT NULL,
  serie VARCHAR(3) NOT NULL,
  PRIMARY KEY (pk_cod_escola)
) ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table aluno
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS aluno (
  pk_cod_pessoa INT NOT NULL AUTO_INCREMENT,
  fk_cod_admin INT NOT NULL,
  fk_cod_escola INT NOT NULL,
  cpf CHAR(11) NOT NULL,
  data_acolhimento DATE NOT NULL,
  form_acesso VARCHAR(15) NOT NULL,
  vacinacao TINYINT NOT NULL,
  termo_imagem TINYINT NOT NULL,
  turno VARCHAR(6) NOT NULL,
  transporte VARCHAR(45) NULL,
  data_nasc DATE NOT NULL,
  proj_ferias TINYINT NOT NULL,
  nome VARCHAR(45) NOT NULL,
  sexo CHAR(1) NOT NULL,
  tamanho_vest VARCHAR(3) NOT NULL,
  tamanho_calc INT NOT NULL,
  turma VARCHAR(3) NULL,
  num_nis CHAR(11) NULL,
  med_paresp VARCHAR(45) NULL,
  alergias VARCHAR(45) NULL,
  observacoes VARCHAR(45) NULL,
  intervencoes VARCHAR(45) NULL,
  evolucoes VARCHAR(45) NULL,
  status INT NOT NULL,
  PRIMARY KEY (pk_cod_pessoa),
  CONSTRAINT fk_aluno_administrador
    FOREIGN KEY (fk_cod_admin)
    REFERENCES administrador (pk_cod_admin)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_aluno_escola1
    FOREIGN KEY (fk_cod_escola)
    REFERENCES escola (pk_cod_escola)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

CREATE UNIQUE INDEX cpf_UNIQUE ON aluno (cpf ASC);
CREATE INDEX fk_aluno_administrador_idx ON aluno (fk_cod_admin ASC);
CREATE INDEX fk_aluno_escola1_idx ON aluno (fk_cod_escola ASC);


-- -----------------------------------------------------
-- Table familia
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS familia (
  pk_cod_familia INT NOT NULL AUTO_INCREMENT,
  fk_cod_pessoa INT NOT NULL,
  qnt_integrantes_fam INT NOT NULL,
  renda_familiar_total DECIMAL(8,2) NOT NULL,
  bolsa_familia DECIMAL(8,2) NULL,
  endereco_familia VARCHAR(45) NOT NULL,
  bairro_familia VARCHAR(20) NOT NULL,
  residencia VARCHAR(12) NOT NULL,
  valor_aluguel DECIMAL(8,2) NULL,
  telefone_familia CHAR(11) NOT NULL,
  PRIMARY KEY (pk_cod_familia),
  CONSTRAINT fk_familia_aluno1
    FOREIGN KEY (fk_cod_pessoa)
    REFERENCES aluno (pk_cod_pessoa)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

CREATE INDEX fk_familia_aluno1_idx ON familia (fk_cod_pessoa ASC);


-- -----------------------------------------------------
-- Table integrante_familia
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS integrante_familia (
  pk_cod_integrante INT NOT NULL AUTO_INCREMENT,
  fk_cod_familia INT NOT NULL,
  nome VARCHAR(45) NOT NULL,
  cpf CHAR(11) NOT NULL,
  parentesco VARCHAR(45) NULL,
  vinculo_afetivo TINYINT NOT NULL,
  ocupacao VARCHAR(45) NULL,
  endereco VARCHAR(45) NULL,
  telefone CHAR(11) NULL,
  resp_legal TINYINT NOT NULL,
  pessoa_autorizada TINYINT NOT NULL,
  PRIMARY KEY (pk_cod_integrante),
  CONSTRAINT fk_integrante_familia_familia1
    FOREIGN KEY (fk_cod_familia)
    REFERENCES familia (pk_cod_familia)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
) ENGINE = InnoDB;

CREATE INDEX fk_integrante_familia_familia1_idx ON integrante_familia (fk_cod_familia ASC);


-- aluno fantasma para teste

-- Inserindo um administrador
INSERT INTO administrador (cpf, senha, telefone, nome)
VALUES ('12345678901', '12345678', '51999999999', 'Admin Principal');

-- Inserindo uma escola
INSERT INTO escola (pk_cod_escola, nome, serie)
VALUES (1, 'Escola Municipal Modelo', '5A');

-- Agora inserindo o aluno Gustavo
INSERT INTO aluno (
    fk_cod_admin,
    fk_cod_escola,
    cpf,
    data_acolhimento,
    form_acesso,
    vacinacao,
    termo_imagem,
    turno,
    transporte,
    data_nasc,
    proj_ferias,
    nome,
    sexo,
    tamanho_vest,
    tamanho_calc,
    turma,
    num_nis,
    med_paresp,
    alergias,
    observacoes,
    intervencoes,
    evolucoes,
    status
) VALUES (
    1, -- fk_cod_admin (Admin cadastrado acima)
    1, -- fk_cod_escola (Escola cadastrada acima)
    '98765432100', -- CPF do aluno
    '2025-09-05', -- data_acolhimento
    'Cadastro',    -- form_acesso
    1, -- vacinacao (1 = sim)
    1, -- termo_imagem (1 = sim)
    'Manhã',
    'Ônibus Escolar',
    '2010-05-15', -- data_nasc (Gustavo nasceu em 15/05/2010, exemplo)
    1, -- proj_ferias (1 = participa)
    'Gustavo',
    'M',
    'M',   -- tamanho_vest
    40,    -- tamanho_calc
    '5A', -- turma
    '12345678910', -- num_nis
    NULL, -- med_paresp
    NULL, -- alergias
    NULL, -- observacoes
    NULL, -- intervencoes
    NULL, -- evolucoes
    1      -- status (1 = ativo)
);
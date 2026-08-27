CREATE TABLE IF NOT EXISTS public.tipo_usuario (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS public.situacao_cadastro (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS public.usuario (
    id SERIAL PRIMARY KEY,
    login VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR NOT NULL,
    id_tipousuario INTEGER NOT NULL,
    FOREIGN KEY (id_tipousuario) REFERENCES public.tipo_usuario(id)
);

CREATE TABLE IF NOT EXISTS public.medico (
    id SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    crm VARCHAR(20) NOT NULL UNIQUE,
    especialidade VARCHAR(100) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_situacaocadastro INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (id_usuario) REFERENCES public.usuario(id),
    FOREIGN KEY (id_situacaocadastro) REFERENCES public.situacao_cadastro(id)
);

CREATE TABLE IF NOT EXISTS public.enfermeiro (
    id SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    coren VARCHAR(20) NOT NULL UNIQUE,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_situacaocadastro INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (id_usuario) REFERENCES public.usuario(id),
    FOREIGN KEY (id_situacaocadastro) REFERENCES public.situacao_cadastro(id)
);

CREATE TABLE IF NOT EXISTS public.recepcionista (
    id SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_situacaocadastro INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (id_usuario) REFERENCES public.usuario(id),
    FOREIGN KEY (id_situacaocadastro) REFERENCES public.situacao_cadastro(id)
);

CREATE TABLE IF NOT EXISTS public.paciente (
    id SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(15) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_situacaocadastro INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (id_usuario) REFERENCES public.usuario(id),
    FOREIGN KEY (id_situacaocadastro) REFERENCES public.situacao_cadastro(id)
);

CREATE TABLE IF NOT EXISTS public.historico_paciente (
    id SERIAL PRIMARY KEY,
    id_paciente INTEGER NOT NULL,
    queixa_principal VARCHAR(500),
    historico_doenca TEXT NOT NULL,
    medicamentos TEXT NOT NULL,
    alergias TEXT NOT NULL,
    observacoes TEXT,
    FOREIGN KEY (id_paciente) REFERENCES public.paciente(id)
);

CREATE TABLE IF NOT EXISTS public.agendamento (
    id SERIAL PRIMARY KEY,
    id_medico INTEGER NOT NULL,
    id_paciente INTEGER NOT NULL,
    datahora_consulta TIMESTAMP NOT NULL,
    observacao TEXT,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_medico) REFERENCES public.medico(id),
    FOREIGN KEY (id_paciente) REFERENCES public.paciente(id),
    CONSTRAINT uk_agendamento_medico_horario UNIQUE (id_medico, datahora_consulta)
);
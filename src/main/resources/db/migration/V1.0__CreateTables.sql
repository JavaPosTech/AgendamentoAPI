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

CREATE TABLE IF NOT EXISTS public.paciente (
    id SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    sobrenome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    endereco VARCHAR(255) NOT NULL,
    data_nascimento DATE NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_situacaocadastro INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (id_usuario) REFERENCES public.usuario(id),
    FOREIGN KEY (id_situacaocadastro) REFERENCES public.situacao_cadastro(id)
);
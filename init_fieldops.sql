-- Script DDL para o Banco de Dados FieldOps (PostgreSQL)

-- Extensão para UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. MÓDULO DE GESTÃO DE ACESSO

CREATE TABLE perfil (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    descricao TEXT,
    tipo_perfil INT UNIQUE NOT NULL
);

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    razao_social VARCHAR(150) NOT NULL,
    cnpj VARCHAR(20) UNIQUE,
    contato VARCHAR(100) NOT NULL
);

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    perfil_id INT NOT NULL REFERENCES perfil(id),
    cliente_id INT REFERENCES clientes(id) ON DELETE SET NULL,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. MÓDULO DE ATIVOS E LOCAIS

CREATE TABLE local (
    id SERIAL PRIMARY KEY,
    cliente_id INT NOT NULL REFERENCES clientes(id) ON DELETE CASCADE,
    nome VARCHAR(100) NOT NULL,
    endereco TEXT NOT NULL
);

CREATE TABLE equipamento (
    id SERIAL PRIMARY KEY,
    local_id INT NOT NULL REFERENCES local(id) ON DELETE CASCADE,
    codigo_qr VARCHAR(100) UNIQUE,
    numero_serie VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL
);

-- 3. MÓDULO DE CHECKLISTS E VERSIONAMENTO

CREATE TABLE modelos_de_inspecao (
    id SERIAL PRIMARY KEY,
    criador_id INT NOT NULL REFERENCES usuarios(id),
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE versoes_de_modelo (
    id SERIAL PRIMARY KEY,
    modelo_id INT NOT NULL REFERENCES modelos_de_inspecao(id) ON DELETE CASCADE,
    numero_versao INT NOT NULL,
    status VARCHAR(30) NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE itens_modelo (
    id SERIAL PRIMARY KEY,
    versao_modelo_id INT NOT NULL REFERENCES versoes_de_modelo(id) ON DELETE CASCADE,
    ordem INT NOT NULL,
    titulo_item VARCHAR(255) NOT NULL,
    tipo_resposta VARCHAR(30) NOT NULL,
    obrigatorio BOOLEAN NOT NULL DEFAULT TRUE
);

-- 4. MÓDULO DE EXECUÇÃO OPERACIONAL

CREATE TABLE atribuicoes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    equipamento_id INT NOT NULL REFERENCES equipamento(id),
    versao_modelo_id INT NOT NULL REFERENCES versoes_de_modelo(id),
    tecnico_id INT NOT NULL REFERENCES usuarios(id),
    supervisor_id INT REFERENCES usuarios(id),
    status VARCHAR(30) NOT NULL,
    data_agendamento TIMESTAMP NOT NULL,
    data_inicio TIMESTAMP,
    data_conclusao TIMESTAMP,
    latitude_execucao DECIMAL(10,8) NOT NULL,
    longitude_execucao DECIMAL(11,8) NOT NULL,
    observacoes_revisao TEXT
);

CREATE TABLE respostas (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    atribuicao_id UUID NOT NULL REFERENCES atribuicoes(id) ON DELETE CASCADE,
    item_modelo_id INT NOT NULL REFERENCES itens_modelo(id),
    valor_resposta TEXT NOT NULL,
    observacao TEXT,
    respondido_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE evidencias (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    resposta_inspecao_id UUID NOT NULL REFERENCES respostas(id) ON DELETE CASCADE,
    caminho_arquivo VARCHAR(255) NOT NULL,
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE nao_conformidades (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    atribuicao_id UUID NOT NULL REFERENCES atribuicoes(id) ON DELETE CASCADE,
    item_modelo_id INT NOT NULL REFERENCES itens_modelo(id),
    descricao_problema TEXT NOT NULL,
    severidade VARCHAR(30) NOT NULL,
    status_resolucao VARCHAR(30) NOT NULL
);

-- 5. MÓDULO DE AUDITORIA E SINCRONIZAÇÃO

CREATE TABLE logs_sincronizacao (
    id BIGSERIAL PRIMARY KEY,
    atribuicao_id UUID NOT NULL REFERENCES atribuicoes(id) ON DELETE CASCADE,
    usuario_id INT NOT NULL REFERENCES usuarios(id),
    dispositivo_uuid VARCHAR(255) NOT NULL,
    data_geracao_offline TIMESTAMP NOT NULL,
    data_sincronizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status_sync VARCHAR(30) NOT NULL
);

CREATE TABLE historico_auditoria (
    id BIGSERIAL PRIMARY KEY,
    usuario_id INT REFERENCES usuarios(id) ON DELETE SET NULL,
    entidade_afetada VARCHAR(100) NOT NULL,
    entidade_id VARCHAR(36) NOT NULL,
    acao VARCHAR(20) NOT NULL,
    dados_anteriores JSONB,
    dados_novos JSONB,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
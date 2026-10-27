
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;


ALTER SCHEMA public OWNER TO postgres;



COMMENT ON SCHEMA public IS '';


SET default_tablespace = '';

SET default_table_access_method = heap;


CREATE TABLE public.clientes (
    id bigint NOT NULL,
    ativo boolean NOT NULL,
    cnpj character varying(20) NOT NULL,
    email character varying(100) NOT NULL,
    nome character varying(150) NOT NULL,
    telefone character varying(20) NOT NULL
);


ALTER TABLE public.clientes OWNER TO postgres;



CREATE SEQUENCE public.clientes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.clientes_id_seq OWNER TO postgres;



ALTER SEQUENCE public.clientes_id_seq OWNED BY public.clientes.id;




CREATE TABLE public.equipamentos (
    id bigint NOT NULL,
    ativo boolean NOT NULL,
    nome character varying(150) NOT NULL,
    numero_serie character varying(100) NOT NULL,
    tipo character varying(100),
    local_id bigint NOT NULL,
    qr_code character varying(100)
);


ALTER TABLE public.equipamentos OWNER TO postgres;



CREATE SEQUENCE public.equipamentos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.equipamentos_id_seq OWNER TO postgres;



ALTER SEQUENCE public.equipamentos_id_seq OWNED BY public.equipamentos.id;




CREATE TABLE public.inspecoes (
    id bigint NOT NULL,
    data_agendada timestamp(6) without time zone NOT NULL,
    data_realizacao timestamp(6) without time zone,
    descricao character varying(255) NOT NULL,
    observacoes character varying(255),
    status character varying(255) NOT NULL,
    equipamento_id bigint NOT NULL,
    usuario_id bigint NOT NULL,
    data_cancelamento timestamp(6) without time zone,
    data_revisao timestamp(6) without time zone,
    latitude double precision,
    longitude double precision,
    motivo_cancelamento character varying(255),
    motivo_reprovacao character varying(255),
    precisao double precision,
    cancelado_por_usuario_id bigint,
    modelo_inspecao_id bigint,
    supervisor_id bigint,
    CONSTRAINT inspecoes_status_check CHECK (((status)::text = ANY ((ARRAY['PENDENTE'::character varying, 'EM_ANDAMENTO'::character varying, 'CONCLUIDA'::character varying, 'CANCELADA'::character varying])::text[])))
);


ALTER TABLE public.inspecoes OWNER TO postgres;



CREATE SEQUENCE public.inspecoes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.inspecoes_id_seq OWNER TO postgres;



ALTER SEQUENCE public.inspecoes_id_seq OWNED BY public.inspecoes.id;



CREATE TABLE public.inspecoes_snapshot_itens (
    id bigint NOT NULL,
    critico boolean NOT NULL,
    item_descricao character varying(255) NOT NULL,
    item_original_id bigint,
    obrigatorio boolean NOT NULL,
    ordem integer NOT NULL,
    secao_titulo character varying(150) NOT NULL,
    tipo_resposta character varying(50) NOT NULL,
    inspecao_id bigint NOT NULL
);


ALTER TABLE public.inspecoes_snapshot_itens OWNER TO postgres;



CREATE SEQUENCE public.inspecoes_snapshot_itens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.inspecoes_snapshot_itens_id_seq OWNER TO postgres;


ALTER SEQUENCE public.inspecoes_snapshot_itens_id_seq OWNED BY public.inspecoes_snapshot_itens.id;


CREATE TABLE public.itens_inspecao (
    id bigint NOT NULL,
    critico boolean NOT NULL,
    descricao character varying(255) NOT NULL,
    obrigatorio boolean NOT NULL,
    ordem integer NOT NULL,
    tipo_resposta character varying(50) NOT NULL,
    secao_id bigint NOT NULL
);


ALTER TABLE public.itens_inspecao OWNER TO postgres;


CREATE SEQUENCE public.itens_inspecao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.itens_inspecao_id_seq OWNER TO postgres;


ALTER SEQUENCE public.itens_inspecao_id_seq OWNED BY public.itens_inspecao.id;


CREATE TABLE public.locais (
    id bigint NOT NULL,
    ativo boolean NOT NULL,
    endereco character varying(255),
    nome character varying(150) NOT NULL,
    cliente_id bigint NOT NULL
);


ALTER TABLE public.locais OWNER TO postgres;


CREATE SEQUENCE public.locais_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.locais_id_seq OWNER TO postgres;


ALTER SEQUENCE public.locais_id_seq OWNED BY public.locais.id;


CREATE TABLE public.modelos_inspecao (
    id bigint NOT NULL,
    categoria character varying(100) NOT NULL,
    data_criacao timestamp(6) without time zone NOT NULL,
    status character varying(50) NOT NULL,
    titulo character varying(150) NOT NULL,
    versao integer NOT NULL
);


ALTER TABLE public.modelos_inspecao OWNER TO postgres;


CREATE SEQUENCE public.modelos_inspecao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.modelos_inspecao_id_seq OWNER TO postgres;


ALTER SEQUENCE public.modelos_inspecao_id_seq OWNED BY public.modelos_inspecao.id;


CREATE TABLE public.nao_conformidades (
    id bigint NOT NULL,
    criticidade character varying(50) NOT NULL,
    data_registro timestamp(6) without time zone NOT NULL,
    descricao character varying(255) NOT NULL,
    inspecao_id bigint NOT NULL,
    resposta_id bigint
);


ALTER TABLE public.nao_conformidades OWNER TO postgres;


CREATE SEQUENCE public.nao_conformidades_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.nao_conformidades_id_seq OWNER TO postgres;


ALTER SEQUENCE public.nao_conformidades_id_seq OWNED BY public.nao_conformidades.id;


CREATE TABLE public.refresh_tokens (
    id bigint NOT NULL,
    data_expiracao timestamp(6) with time zone NOT NULL,
    revogado boolean NOT NULL,
    token character varying(255) NOT NULL,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.refresh_tokens OWNER TO postgres;


CREATE SEQUENCE public.refresh_tokens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.refresh_tokens_id_seq OWNER TO postgres;


ALTER SEQUENCE public.refresh_tokens_id_seq OWNED BY public.refresh_tokens.id;


CREATE TABLE public.registros_sincronizacao (
    id bigint NOT NULL,
    chave_idempotencia character varying(255) NOT NULL,
    data_recebimento timestamp(6) without time zone NOT NULL,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.registros_sincronizacao OWNER TO postgres;

CREATE SEQUENCE public.registros_sincronizacao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.registros_sincronizacao_id_seq OWNER TO postgres;


ALTER SEQUENCE public.registros_sincronizacao_id_seq OWNED BY public.registros_sincronizacao.id;

CREATE TABLE public.respostas_inspecao (
    id bigint NOT NULL,
    inspecao_id bigint NOT NULL,
    item_modelo_id bigint NOT NULL,
    valor character varying(255),
    observacao character varying(255)
);


ALTER TABLE public.respostas_inspecao OWNER TO postgres;


CREATE SEQUENCE public.respostas_inspecao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.respostas_inspecao_id_seq OWNER TO postgres;
ALTER SEQUENCE public.respostas_inspecao_id_seq OWNED BY public.respostas_inspecao.id;


CREATE TABLE public.secoes_inspecao (
    id bigint NOT NULL,
    ordem integer NOT NULL,
    titulo character varying(150) NOT NULL,
    modelo_id bigint NOT NULL
);


ALTER TABLE public.secoes_inspecao OWNER TO postgres;


CREATE SEQUENCE public.secoes_inspecao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.secoes_inspecao_id_seq OWNER TO postgres;

ALTER SEQUENCE public.secoes_inspecao_id_seq OWNED BY public.secoes_inspecao.id;

CREATE TABLE public.tb_fotos_inspecao (
    id bigint NOT NULL,
    caminho_arquivo character varying(255) NOT NULL,
    data_upload timestamp(6) without time zone NOT NULL,
    nome_arquivo character varying(255) NOT NULL,
    tipo_conteudo character varying(255) NOT NULL,
    inspecao_id bigint NOT NULL
);


ALTER TABLE public.tb_fotos_inspecao OWNER TO postgres;


CREATE SEQUENCE public.tb_fotos_inspecao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tb_fotos_inspecao_id_seq OWNER TO postgres;


ALTER SEQUENCE public.tb_fotos_inspecao_id_seq OWNED BY public.tb_fotos_inspecao.id;


CREATE TABLE public.usuarios (
    id bigint NOT NULL,
    ativo boolean,
    email character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    perfil character varying(255) NOT NULL,
    senha character varying(255) NOT NULL,
    CONSTRAINT usuarios_perfil_check CHECK (((perfil)::text = ANY ((ARRAY['ADMINISTRADOR'::character varying, 'SUPERVISOR'::character varying, 'TECNICO'::character varying])::text[])))
);


ALTER TABLE public.usuarios OWNER TO postgres;


CREATE SEQUENCE public.usuarios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuarios_id_seq OWNER TO postgres;


ALTER SEQUENCE public.usuarios_id_seq OWNED BY public.usuarios.id;


ALTER TABLE ONLY public.clientes ALTER COLUMN id SET DEFAULT nextval('public.clientes_id_seq'::regclass);


ALTER TABLE ONLY public.equipamentos ALTER COLUMN id SET DEFAULT nextval('public.equipamentos_id_seq'::regclass);


ALTER TABLE ONLY public.inspecoes ALTER COLUMN id SET DEFAULT nextval('public.inspecoes_id_seq'::regclass);


ALTER TABLE ONLY public.inspecoes_snapshot_itens ALTER COLUMN id SET DEFAULT nextval('public.inspecoes_snapshot_itens_id_seq'::regclass);


ALTER TABLE ONLY public.itens_inspecao ALTER COLUMN id SET DEFAULT nextval('public.itens_inspecao_id_seq'::regclass);



ALTER TABLE ONLY public.locais ALTER COLUMN id SET DEFAULT nextval('public.locais_id_seq'::regclass);


ALTER TABLE ONLY public.modelos_inspecao ALTER COLUMN id SET DEFAULT nextval('public.modelos_inspecao_id_seq'::regclass);


ALTER TABLE ONLY public.nao_conformidades ALTER COLUMN id SET DEFAULT nextval('public.nao_conformidades_id_seq'::regclass);

ALTER TABLE ONLY public.refresh_tokens ALTER COLUMN id SET DEFAULT nextval('public.refresh_tokens_id_seq'::regclass);


ALTER TABLE ONLY public.registros_sincronizacao ALTER COLUMN id SET DEFAULT nextval('public.registros_sincronizacao_id_seq'::regclass);


ALTER TABLE ONLY public.respostas_inspecao ALTER COLUMN id SET DEFAULT nextval('public.respostas_inspecao_id_seq'::regclass);

ALTER TABLE ONLY public.secoes_inspecao ALTER COLUMN id SET DEFAULT nextval('public.secoes_inspecao_id_seq'::regclass);


ALTER TABLE ONLY public.tb_fotos_inspecao ALTER COLUMN id SET DEFAULT nextval('public.tb_fotos_inspecao_id_seq'::regclass);


ALTER TABLE ONLY public.usuarios ALTER COLUMN id SET DEFAULT nextval('public.usuarios_id_seq'::regclass);



COPY public.clientes (id, ativo, cnpj, email, nome, telefone) FROM stdin;
99	t	12345678000199	contato@silva.com	Indústrias Metalúrgicas Silva LTDA	(11) 99999-9999
\.


COPY public.equipamentos (id, ativo, nome, numero_serie, tipo, local_id, qr_code) FROM stdin;
99	t	Torno CNC Romi Centur 30D	ROMI-998877	Torno	99	QR-99
\.


COPY public.inspecoes (id, data_agendada, data_realizacao, descricao, observacoes, status, equipamento_id, usuario_id, data_cancelamento, data_revisao, latitude, longitude, motivo_cancelamento, motivo_reprovacao, precisao, cancelado_por_usuario_id, modelo_inspecao_id, supervisor_id) FROM stdin;
\.



COPY public.inspecoes_snapshot_itens (id, critico, item_descricao, item_original_id, obrigatorio, ordem, secao_titulo, tipo_resposta, inspecao_id) FROM stdin;
\.



COPY public.itens_inspecao (id, critico, descricao, obrigatorio, ordem, tipo_resposta, secao_id) FROM stdin;
1	t	O disjuntor principal está sinalizado?	t	1	BOOLEAN	1
\.




COPY public.locais (id, ativo, endereco, nome, cliente_id) FROM stdin;
99	t	Av. Industrial, 1000	Fábrica Principal - Setor de Usinagem	99
\.



COPY public.modelos_inspecao (id, categoria, data_criacao, status, titulo, versao) FROM stdin;
1	Manutenção	2026-10-23 14:48:02.863435	PUBLICADO	Inspeção de Segurança Elétrica	1
\.


COPY public.nao_conformidades (id, criticidade, data_registro, descricao, inspecao_id, resposta_id) FROM stdin;
\.



COPY public.refresh_tokens (id, data_expiracao, revogado, token, usuario_id) FROM stdin;
2	2026-10-24 16:11:49.599828+00	f	4450cfbb-a8f4-4c55-817c-1e97c11fc383	1
\.



COPY public.registros_sincronizacao (id, chave_idempotencia, data_recebimento, usuario_id) FROM stdin;
\.


COPY public.respostas_inspecao (id, inspecao_id, item_modelo_id, valor, observacao) FROM stdin;
\.



COPY public.secoes_inspecao (id, ordem, titulo, modelo_id) FROM stdin;
1	1	Verificação de Quadros	1
\.



COPY public.tb_fotos_inspecao (id, caminho_arquivo, data_upload, nome_arquivo, tipo_conteudo, inspecao_id) FROM stdin;
\.




COPY public.usuarios (id, ativo, email, nome, perfil, senha) FROM stdin;
1	t	admin@fieldops.com	Admin	ADMINISTRADOR	$2a$10$6jxEAVsqwzf9ejKbk.rNw.EUDAeOHJ1eG.N/lOG0gURnw3eNmXGDG
\.



SELECT pg_catalog.setval('public.clientes_id_seq', 1, false);



SELECT pg_catalog.setval('public.equipamentos_id_seq', 1, false);



SELECT pg_catalog.setval('public.inspecoes_id_seq', 1, false);



SELECT pg_catalog.setval('public.inspecoes_snapshot_itens_id_seq', 1, false);



SELECT pg_catalog.setval('public.itens_inspecao_id_seq', 1, true);



SELECT pg_catalog.setval('public.locais_id_seq', 1, false);



SELECT pg_catalog.setval('public.modelos_inspecao_id_seq', 1, true);




SELECT pg_catalog.setval('public.nao_conformidades_id_seq', 1, false);



SELECT pg_catalog.setval('public.refresh_tokens_id_seq', 2, true);



SELECT pg_catalog.setval('public.registros_sincronizacao_id_seq', 1, false);



SELECT pg_catalog.setval('public.respostas_inspecao_id_seq', 1, false);



SELECT pg_catalog.setval('public.secoes_inspecao_id_seq', 1, true);



SELECT pg_catalog.setval('public.tb_fotos_inspecao_id_seq', 1, false);



SELECT pg_catalog.setval('public.usuarios_id_seq', 1, true);


ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT clientes_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.equipamentos
    ADD CONSTRAINT equipamentos_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.inspecoes
    ADD CONSTRAINT inspecoes_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.inspecoes_snapshot_itens
    ADD CONSTRAINT inspecoes_snapshot_itens_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.itens_inspecao
    ADD CONSTRAINT itens_inspecao_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.locais
    ADD CONSTRAINT locais_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.modelos_inspecao
    ADD CONSTRAINT modelos_inspecao_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.nao_conformidades
    ADD CONSTRAINT nao_conformidades_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT refresh_tokens_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.registros_sincronizacao
    ADD CONSTRAINT registros_sincronizacao_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.respostas_inspecao
    ADD CONSTRAINT respostas_inspecao_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.secoes_inspecao
    ADD CONSTRAINT secoes_inspecao_pkey PRIMARY KEY (id);



ALTER TABLE ONLY public.tb_fotos_inspecao
    ADD CONSTRAINT tb_fotos_inspecao_pkey PRIMARY KEY (id);




ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT uk_1c96wv36rk2hwui7qhjks3mvg UNIQUE (email);




ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT uk_8tpffwedgo0fmjonjwho3l8vt UNIQUE (cnpj);



ALTER TABLE ONLY public.equipamentos
    ADD CONSTRAINT uk_a1u99ovhp1a5gkeoyoagnbm90 UNIQUE (numero_serie);




ALTER TABLE ONLY public.equipamentos
    ADD CONSTRAINT uk_ge8lx4r7ahp0vyk7qkykgv2v0 UNIQUE (qr_code);



ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT uk_ghpmfn23vmxfu3spu3lfg4r2d UNIQUE (token);



ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT uk_k67ke3il4jxwu6wpqldns594d UNIQUE (usuario_id);


ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT uk_kfsp0s1tflm1cwlj8idhqsad0 UNIQUE (email);




ALTER TABLE ONLY public.registros_sincronizacao
    ADD CONSTRAINT uk_sl7c5bs8tq51pl42uenp9nes2 UNIQUE (chave_idempotencia);




ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);




ALTER TABLE ONLY public.equipamentos
    ADD CONSTRAINT fk29n91q86l475um3n1olyeix17 FOREIGN KEY (local_id) REFERENCES public.locais(id);




ALTER TABLE ONLY public.inspecoes_snapshot_itens
    ADD CONSTRAINT fk4g0a80r4nu86g9lpr928u4174 FOREIGN KEY (inspecao_id) REFERENCES public.inspecoes(id);




ALTER TABLE ONLY public.nao_conformidades
    ADD CONSTRAINT fk4ks7jvy0c27i9ap70iwy0okmk FOREIGN KEY (resposta_id) REFERENCES public.respostas_inspecao(id);



ALTER TABLE ONLY public.inspecoes
    ADD CONSTRAINT fk4rs23k7e0a9wnu98dins2ua41 FOREIGN KEY (cancelado_por_usuario_id) REFERENCES public.usuarios(id);



ALTER TABLE ONLY public.tb_fotos_inspecao
    ADD CONSTRAINT fk70bkvtxfpyhdh0qk0rfh48evp FOREIGN KEY (inspecao_id) REFERENCES public.inspecoes(id);

ALTER TABLE ONLY public.respostas_inspecao
    ADD CONSTRAINT fk8bfnprv311fp3rept3iyn9flw FOREIGN KEY (inspecao_id) REFERENCES public.inspecoes(id);



ALTER TABLE ONLY public.respostas_inspecao
    ADD CONSTRAINT fk9dhdudh0vulxq4p296yj8bleg FOREIGN KEY (item_modelo_id) REFERENCES public.inspecoes_snapshot_itens(id);



ALTER TABLE ONLY public.registros_sincronizacao
    ADD CONSTRAINT fk9qi6v5bbryhbvqjg4i3nag4ek FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


ALTER TABLE ONLY public.secoes_inspecao
    ADD CONSTRAINT fkarqpriqqj18tdihv9xjo9gfdg FOREIGN KEY (modelo_id) REFERENCES public.modelos_inspecao(id);


ALTER TABLE ONLY public.nao_conformidades
    ADD CONSTRAINT fkcnhb5yv1ta61u33d5buxjvcm3 FOREIGN KEY (inspecao_id) REFERENCES public.inspecoes(id);



ALTER TABLE ONLY public.inspecoes
    ADD CONSTRAINT fkgnpgjdb79v70m9undug9yn2d5 FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


ALTER TABLE ONLY public.inspecoes
    ADD CONSTRAINT fkgobq44lunj2b19dd9yey3momb FOREIGN KEY (equipamento_id) REFERENCES public.equipamentos(id);



ALTER TABLE ONLY public.inspecoes
    ADD CONSTRAINT fkinc3j3y8yubfu88exra527lh3 FOREIGN KEY (supervisor_id) REFERENCES public.usuarios(id);



ALTER TABLE ONLY public.itens_inspecao
    ADD CONSTRAINT fkjgc30dwf0uwcynih04qwcbyle FOREIGN KEY (secao_id) REFERENCES public.secoes_inspecao(id);



ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT fkpdrw1klic7bvvhhkjojwu64t2 FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);



ALTER TABLE ONLY public.inspecoes
    ADD CONSTRAINT fkrci8umsjnqtag2i0stv6v5jkc FOREIGN KEY (modelo_inspecao_id) REFERENCES public.modelos_inspecao(id);



ALTER TABLE ONLY public.locais
    ADD CONSTRAINT fktei7nk62rswrtnfwh8hlu38wi FOREIGN KEY (cliente_id) REFERENCES public.clientes(id);


REVOKE USAGE ON SCHEMA public FROM PUBLIC;

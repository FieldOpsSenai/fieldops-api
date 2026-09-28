--
-- PostgreSQL database dump
--

\restrict W0yEHeTh5H8hbNhDpn5fRngUh7hu12B7KD0e2bfCf8FXXUFt9Ihpw1HrNrcFBeQ

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

-- *not* creating schema, since initdb creates it


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS '';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: clientes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.clientes (
    id bigint NOT NULL,
    ativo boolean NOT NULL,
    cnpj character varying(20) NOT NULL,
    email character varying(100) NOT NULL,
    nome character varying(150) NOT NULL,
    telefone character varying(20) NOT NULL
);


ALTER TABLE public.clientes OWNER TO postgres;

--
-- Name: clientes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.clientes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.clientes_id_seq OWNER TO postgres;

--
-- Name: clientes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.clientes_id_seq OWNED BY public.clientes.id;


--
-- Name: equipamentos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.equipamentos (
    id bigint NOT NULL,
    ativo boolean NOT NULL,
    nome character varying(150) NOT NULL,
    numero_serie character varying(100) NOT NULL,
    tipo character varying(100),
    local_id bigint NOT NULL
);


ALTER TABLE public.equipamentos OWNER TO postgres;

--
-- Name: equipamentos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.equipamentos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.equipamentos_id_seq OWNER TO postgres;

--
-- Name: equipamentos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.equipamentos_id_seq OWNED BY public.equipamentos.id;


--
-- Name: inspecoes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.inspecoes (
    id bigint NOT NULL,
    data_agendada timestamp(6) without time zone NOT NULL,
    data_realizacao timestamp(6) without time zone,
    descricao character varying(255) NOT NULL,
    observacoes character varying(255),
    status character varying(255) NOT NULL,
    equipamento_id bigint NOT NULL,
    usuario_id bigint NOT NULL,
    CONSTRAINT inspecoes_status_check CHECK (((status)::text = ANY ((ARRAY['PENDENTE'::character varying, 'EM_ANDAMENTO'::character varying, 'CONCLUIDA'::character varying, 'CANCELADA'::character varying])::text[])))
);


ALTER TABLE public.inspecoes OWNER TO postgres;

--
-- Name: inspecoes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.inspecoes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.inspecoes_id_seq OWNER TO postgres;

--
-- Name: inspecoes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.inspecoes_id_seq OWNED BY public.inspecoes.id;


--
-- Name: locais; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.locais (
    id bigint NOT NULL,
    ativo boolean NOT NULL,
    endereco character varying(255),
    nome character varying(150) NOT NULL,
    cliente_id bigint NOT NULL
);


ALTER TABLE public.locais OWNER TO postgres;

--
-- Name: locais_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.locais_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.locais_id_seq OWNER TO postgres;

--
-- Name: locais_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.locais_id_seq OWNED BY public.locais.id;


--
-- Name: refresh_tokens; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.refresh_tokens (
    id bigint NOT NULL,
    data_expiracao timestamp(6) with time zone NOT NULL,
    revogado boolean NOT NULL,
    token character varying(255) NOT NULL,
    usuario_id bigint NOT NULL
);


ALTER TABLE public.refresh_tokens OWNER TO postgres;

--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.refresh_tokens_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.refresh_tokens_id_seq OWNER TO postgres;

--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.refresh_tokens_id_seq OWNED BY public.refresh_tokens.id;


--
-- Name: tb_fotos_inspecao; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tb_fotos_inspecao (
    id bigint NOT NULL,
    caminho_arquivo character varying(255) NOT NULL,
    data_upload timestamp(6) without time zone NOT NULL,
    nome_arquivo character varying(255) NOT NULL,
    tipo_conteudo character varying(255) NOT NULL,
    inspecao_id bigint NOT NULL
);


ALTER TABLE public.tb_fotos_inspecao OWNER TO postgres;

--
-- Name: tb_fotos_inspecao_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tb_fotos_inspecao_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tb_fotos_inspecao_id_seq OWNER TO postgres;

--
-- Name: tb_fotos_inspecao_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tb_fotos_inspecao_id_seq OWNED BY public.tb_fotos_inspecao.id;


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: postgres
--

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

--
-- Name: usuarios_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.usuarios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.usuarios_id_seq OWNER TO postgres;

--
-- Name: usuarios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.usuarios_id_seq OWNED BY public.usuarios.id;


--
-- Name: clientes id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientes ALTER COLUMN id SET DEFAULT nextval('public.clientes_id_seq'::regclass);


--
-- Name: equipamentos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipamentos ALTER COLUMN id SET DEFAULT nextval('public.equipamentos_id_seq'::regclass);


--
-- Name: inspecoes id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inspecoes ALTER COLUMN id SET DEFAULT nextval('public.inspecoes_id_seq'::regclass);


--
-- Name: locais id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locais ALTER COLUMN id SET DEFAULT nextval('public.locais_id_seq'::regclass);


--
-- Name: refresh_tokens id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refresh_tokens ALTER COLUMN id SET DEFAULT nextval('public.refresh_tokens_id_seq'::regclass);


--
-- Name: tb_fotos_inspecao id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_fotos_inspecao ALTER COLUMN id SET DEFAULT nextval('public.tb_fotos_inspecao_id_seq'::regclass);


--
-- Name: usuarios id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios ALTER COLUMN id SET DEFAULT nextval('public.usuarios_id_seq'::regclass);


--
-- Data for Name: clientes; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: equipamentos; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: inspecoes; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: locais; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: refresh_tokens; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: tb_fotos_inspecao; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.usuarios VALUES (1, true, 'admin@fieldops.com', 'Admin', 'ADMINISTRADOR', '$2a$10$knZOFYA3epHOL0mUH3g5xOQgpc8FY9DgEkefLjex1ZNNWF2GlwhtO');


--
-- Name: clientes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.clientes_id_seq', 1, false);


--
-- Name: equipamentos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.equipamentos_id_seq', 1, false);


--
-- Name: inspecoes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.inspecoes_id_seq', 1, false);


--
-- Name: locais_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.locais_id_seq', 1, false);


--
-- Name: refresh_tokens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.refresh_tokens_id_seq', 1, false);


--
-- Name: tb_fotos_inspecao_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tb_fotos_inspecao_id_seq', 1, false);


--
-- Name: usuarios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.usuarios_id_seq', 1, true);


--
-- Name: clientes clientes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT clientes_pkey PRIMARY KEY (id);


--
-- Name: equipamentos equipamentos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipamentos
    ADD CONSTRAINT equipamentos_pkey PRIMARY KEY (id);


--
-- Name: inspecoes inspecoes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inspecoes
    ADD CONSTRAINT inspecoes_pkey PRIMARY KEY (id);


--
-- Name: locais locais_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locais
    ADD CONSTRAINT locais_pkey PRIMARY KEY (id);


--
-- Name: refresh_tokens refresh_tokens_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT refresh_tokens_pkey PRIMARY KEY (id);


--
-- Name: tb_fotos_inspecao tb_fotos_inspecao_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_fotos_inspecao
    ADD CONSTRAINT tb_fotos_inspecao_pkey PRIMARY KEY (id);


--
-- Name: clientes uk_1c96wv36rk2hwui7qhjks3mvg; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT uk_1c96wv36rk2hwui7qhjks3mvg UNIQUE (email);


--
-- Name: clientes uk_8tpffwedgo0fmjonjwho3l8vt; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.clientes
    ADD CONSTRAINT uk_8tpffwedgo0fmjonjwho3l8vt UNIQUE (cnpj);


--
-- Name: equipamentos uk_a1u99ovhp1a5gkeoyoagnbm90; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipamentos
    ADD CONSTRAINT uk_a1u99ovhp1a5gkeoyoagnbm90 UNIQUE (numero_serie);


--
-- Name: refresh_tokens uk_ghpmfn23vmxfu3spu3lfg4r2d; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT uk_ghpmfn23vmxfu3spu3lfg4r2d UNIQUE (token);


--
-- Name: refresh_tokens uk_k67ke3il4jxwu6wpqldns594d; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT uk_k67ke3il4jxwu6wpqldns594d UNIQUE (usuario_id);


--
-- Name: usuarios uk_kfsp0s1tflm1cwlj8idhqsad0; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT uk_kfsp0s1tflm1cwlj8idhqsad0 UNIQUE (email);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (id);


--
-- Name: equipamentos fk29n91q86l475um3n1olyeix17; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.equipamentos
    ADD CONSTRAINT fk29n91q86l475um3n1olyeix17 FOREIGN KEY (local_id) REFERENCES public.locais(id);


--
-- Name: tb_fotos_inspecao fk70bkvtxfpyhdh0qk0rfh48evp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tb_fotos_inspecao
    ADD CONSTRAINT fk70bkvtxfpyhdh0qk0rfh48evp FOREIGN KEY (inspecao_id) REFERENCES public.inspecoes(id);


--
-- Name: inspecoes fkgnpgjdb79v70m9undug9yn2d5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inspecoes
    ADD CONSTRAINT fkgnpgjdb79v70m9undug9yn2d5 FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- Name: inspecoes fkgobq44lunj2b19dd9yey3momb; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.inspecoes
    ADD CONSTRAINT fkgobq44lunj2b19dd9yey3momb FOREIGN KEY (equipamento_id) REFERENCES public.equipamentos(id);


--
-- Name: refresh_tokens fkpdrw1klic7bvvhhkjojwu64t2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refresh_tokens
    ADD CONSTRAINT fkpdrw1klic7bvvhhkjojwu64t2 FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);


--
-- Name: locais fktei7nk62rswrtnfwh8hlu38wi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locais
    ADD CONSTRAINT fktei7nk62rswrtnfwh8hlu38wi FOREIGN KEY (cliente_id) REFERENCES public.clientes(id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;


--
-- PostgreSQL database dump complete
--

\unrestrict W0yEHeTh5H8hbNhDpn5fRngUh7hu12B7KD0e2bfCf8FXXUFt9Ihpw1HrNrcFBeQ


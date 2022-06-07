--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.25
-- Dumped by pg_dump version 9.5.5

-- Started on 2022-06-02 03:45:00 -03

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2643 (class 1262 OID 17708)
-- Name: loja_comprebem; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE loja_comprebem WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'C' LC_CTYPE = 'C';


--ALTER DATABASE loja_comprebem OWNER TO postgres;

--\connect loja_comprebem

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12623)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2646 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 221 (class 1255 OID 18080)
-- Name: validachavepessoa(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION validachavepessoa() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare existe integer;



BEGIN

		existe = (select count (1) from pessoa_fisica where id = NEW.pessoa_id);

	if (existe <= 0) then

		existe = (select count(1) from pessoa_juridica where id = NEW.pessoa_id);

	if (existe <= 0) then

		RAISE EXCEPTION 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';

	 end if;

	end if;

 return new;

END;

$$;


ALTER FUNCTION public.validachavepessoa() OWNER TO postgres;

--
-- TOC entry 234 (class 1255 OID 18088)
-- Name: validachavepessoa_forn(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION validachavepessoa_forn() RETURNS trigger
    LANGUAGE plpgsql
    AS $$

declare existe integer;

BEGIN

		existe = (select count (1) from pessoa_fisica where id = NEW.pessoa_fornecedor_id);

	if (existe <= 0) then

		existe = (select count(1) from pessoa_juridica where id = NEW.pessoa_fornecedor_id);

	if (existe <= 0) then

		RAISE EXCEPTION 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';

	 end if;
	end if;
 return new;
END;
$$;


ALTER FUNCTION public.validachavepessoa_forn() OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 219 (class 1259 OID 18068)
-- Name: acesso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE acesso (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL
);


ALTER TABLE acesso OWNER TO postgres;

--
-- TOC entry 181 (class 1259 OID 17714)
-- Name: avaliacao_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE avaliacao_produto (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    nota integer NOT NULL,
    empresa_id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    produto_id bigint NOT NULL
);


ALTER TABLE avaliacao_produto OWNER TO postgres;

--
-- TOC entry 182 (class 1259 OID 17719)
-- Name: categoria_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE categoria_produto (
    id bigint NOT NULL,
    nome_desc character varying(255) NOT NULL,
    empresa_id bigint NOT NULL
);


ALTER TABLE categoria_produto OWNER TO postgres;

--
-- TOC entry 183 (class 1259 OID 17724)
-- Name: conta_pagar; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE conta_pagar (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    dt_pagamento date,
    dt_vencimento date NOT NULL,
    status character varying(255) NOT NULL,
    valor_desconto numeric(19,2),
    valor_total numeric(19,2) NOT NULL,
    empresa_id bigint NOT NULL,
    pessoa_id bigint NOT NULL,
    pessoa_fornecedor_id bigint NOT NULL
);


ALTER TABLE conta_pagar OWNER TO postgres;

--
-- TOC entry 184 (class 1259 OID 17732)
-- Name: conta_receber; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE conta_receber (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    dt_pagamento date,
    dt_vencimento date NOT NULL,
    status character varying(255) NOT NULL,
    valor_desconto numeric(19,2),
    valor_total numeric(19,2) NOT NULL,
    empresa_id bigint NOT NULL,
    pessoa_id bigint NOT NULL
);


ALTER TABLE conta_receber OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 17740)
-- Name: cupom_desconto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cupom_desconto (
    id bigint NOT NULL,
    cod_desc character varying(255) NOT NULL,
    validade_cupom date NOT NULL,
    valor_porcent_desc numeric(19,2),
    valor_real_desc numeric(19,2),
    empresa_id bigint NOT NULL
);


ALTER TABLE cupom_desconto OWNER TO postgres;

--
-- TOC entry 186 (class 1259 OID 17745)
-- Name: endereco; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE endereco (
    id bigint NOT NULL,
    bairro character varying(255) NOT NULL,
    cep character varying(255) NOT NULL,
    cidade character varying(255) NOT NULL,
    complemento character varying(255),
    numero character varying(255) NOT NULL,
    rua_logradouro character varying(255) NOT NULL,
    tipo_endereco character varying(255) NOT NULL,
    uf character varying(255) NOT NULL,
    empresa_id bigint NOT NULL,
    pessoa_id bigint NOT NULL
);


ALTER TABLE endereco OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 17753)
-- Name: forma_pagamento; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE forma_pagamento (
    id bigint NOT NULL,
    descricao character varying(255) NOT NULL,
    empresa_id bigint NOT NULL
);


ALTER TABLE forma_pagamento OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 17758)
-- Name: imagem_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE imagem_produto (
    id bigint NOT NULL,
    imagem_miniatura text NOT NULL,
    imagem_original text NOT NULL,
    empresa_id bigint NOT NULL,
    produto_id bigint NOT NULL
);


ALTER TABLE imagem_produto OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 17766)
-- Name: item_venda_loja; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE item_venda_loja (
    id bigint NOT NULL,
    quantidade double precision NOT NULL,
    venda_compra_loja_virtual bytea,
    empresa_id bigint NOT NULL,
    produto_id bigint NOT NULL
);


ALTER TABLE item_venda_loja OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 17774)
-- Name: marca_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE marca_produto (
    id bigint NOT NULL,
    nome_desc character varying(255) NOT NULL,
    empresa_id bigint NOT NULL
);


ALTER TABLE marca_produto OWNER TO postgres;

--
-- TOC entry 191 (class 1259 OID 17779)
-- Name: nota_fiscal_compra; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE nota_fiscal_compra (
    id bigint NOT NULL,
    data_compra date,
    descricao_observacao character varying(255),
    numero_nota character varying(255) NOT NULL,
    serie_nota character varying(255) NOT NULL,
    valor_desconto numeric(19,2),
    valor_icms numeric(19,2) NOT NULL,
    valor_total numeric(19,2) NOT NULL,
    conta_pagar_id bigint NOT NULL,
    empresa_id bigint NOT NULL,
    pessoa_id bigint NOT NULL
);


ALTER TABLE nota_fiscal_compra OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 17787)
-- Name: nota_fiscal_venda; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE nota_fiscal_venda (
    id bigint NOT NULL,
    numero character varying(255) NOT NULL,
    pdf text NOT NULL,
    serie character varying(255) NOT NULL,
    tipo character varying(255) NOT NULL,
    xml text NOT NULL,
    empresa_id bigint NOT NULL,
    venda_compra_loja_virt_id bigint NOT NULL
);


ALTER TABLE nota_fiscal_venda OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 17795)
-- Name: nota_item_produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE nota_item_produto (
    id bigint NOT NULL,
    quantidade double precision NOT NULL,
    empresa_id bigint NOT NULL,
    nota_fiscal_compra_id bigint NOT NULL,
    produto_id bigint NOT NULL
);


ALTER TABLE nota_item_produto OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 17800)
-- Name: pessoa_fisica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE pessoa_fisica (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    telefone character varying(255) NOT NULL,
    tipo_pessoa character varying(255),
    empresa_id bigint NOT NULL,
    cpf character varying(255) NOT NULL,
    data_nascimento date
);


ALTER TABLE pessoa_fisica OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 17808)
-- Name: pessoa_juridica; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE pessoa_juridica (
    id bigint NOT NULL,
    email character varying(255) NOT NULL,
    nome character varying(255) NOT NULL,
    telefone character varying(255) NOT NULL,
    tipo_pessoa character varying(255),
    empresa_id bigint NOT NULL,
    categoria character varying(255),
    cnpj character varying(255) NOT NULL,
    insc_estadual character varying(255) NOT NULL,
    insc_municipal character varying(255),
    nome_fantasia character varying(255) NOT NULL,
    razao_social character varying(255) NOT NULL
);


ALTER TABLE pessoa_juridica OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 17816)
-- Name: produto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE produto (
    id bigint NOT NULL,
    alerta_qtd_estoque boolean,
    altura double precision NOT NULL,
    ativo boolean,
    descricao text NOT NULL,
    largura double precision NOT NULL,
    link_youtube character varying(255),
    nome character varying(255) NOT NULL,
    peso double precision NOT NULL,
    profundidade double precision NOT NULL,
    qtd_alerta_estoque integer,
    qtd_click integer,
    qtd_estoque integer NOT NULL,
    tipo_unidade character varying(255) NOT NULL,
    valor_venda numeric(19,2) NOT NULL,
    empresa_id bigint NOT NULL,
    nota_item_produto_id bigint NOT NULL
);


ALTER TABLE produto OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 18073)
-- Name: seq_acesso; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_acesso
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_acesso OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 17854)
-- Name: seq_avaliacao_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_avaliacao_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_avaliacao_produto OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 17856)
-- Name: seq_categoria_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_categoria_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_categoria_produto OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 17858)
-- Name: seq_conta_pagar; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_conta_pagar
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_conta_pagar OWNER TO postgres;

--
-- TOC entry 204 (class 1259 OID 17860)
-- Name: seq_conta_receber; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_conta_receber
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_conta_receber OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 17862)
-- Name: seq_cupom_desconto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_cupom_desconto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_cupom_desconto OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 17864)
-- Name: seq_endereco; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_endereco
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_endereco OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 17866)
-- Name: seq_forma_pagamento; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_forma_pagamento
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_forma_pagamento OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 17868)
-- Name: seq_imagem_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_imagem_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_imagem_produto OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 17870)
-- Name: seq_item_venda_loja; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_item_venda_loja
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_item_venda_loja OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 17872)
-- Name: seq_marca_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_marca_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_marca_produto OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 17874)
-- Name: seq_nota_fiscal_compra; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_nota_fiscal_compra
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_nota_fiscal_compra OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 17876)
-- Name: seq_nota_fiscal_venda; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_nota_fiscal_venda
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_nota_fiscal_venda OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 17878)
-- Name: seq_nota_item_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_nota_item_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_nota_item_produto OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 17880)
-- Name: seq_pessoa; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_pessoa
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_pessoa OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 17882)
-- Name: seq_produto; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_produto
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_produto OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 17884)
-- Name: seq_status_rastreio; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_status_rastreio
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_status_rastreio OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 17886)
-- Name: seq_usuario; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_usuario
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_usuario OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 17888)
-- Name: seq_vd_cp_loja_virt; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE seq_vd_cp_loja_virt
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE seq_vd_cp_loja_virt OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 17824)
-- Name: status_rastreio; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE status_rastreio (
    id bigint NOT NULL,
    centro_distruicao character varying(255),
    cidade character varying(255),
    estado character varying(255),
    status character varying(255),
    empresa_id bigint NOT NULL,
    venda_compra_loja_virt_id bigint NOT NULL
);


ALTER TABLE status_rastreio OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 17832)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE usuario (
    id bigint NOT NULL,
    data_atual_senha date NOT NULL,
    login character varying(255) NOT NULL,
    senha character varying(255) NOT NULL,
    empresa_id bigint NOT NULL,
    pessoa_id bigint NOT NULL
);


ALTER TABLE usuario OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 17840)
-- Name: usuarios_acesso; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE usuarios_acesso (
    usuario_id bigint NOT NULL,
    acesso_id bigint NOT NULL
);


ALTER TABLE usuarios_acesso OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 17843)
-- Name: vd_cp_loja_virt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE vd_cp_loja_virt (
    id bigint NOT NULL,
    data_entrega date NOT NULL,
    data_venda date NOT NULL,
    dia_entrega integer NOT NULL,
    valor_desconto numeric(19,2),
    valor_frete numeric(19,2) NOT NULL,
    valor_total numeric(19,2) NOT NULL,
    cupom_desconto_id bigint,
    empresa_id bigint NOT NULL,
    endereco_cobranca_id bigint NOT NULL,
    endereco_entrega_id bigint NOT NULL,
    forma_pagamento_id bigint NOT NULL,
    nota_fiscal_venda_id bigint NOT NULL,
    pessoa_id bigint NOT NULL
);


ALTER TABLE vd_cp_loja_virt OWNER TO postgres;

--
-- TOC entry 2637 (class 0 OID 18068)
-- Dependencies: 219
-- Data for Name: acesso; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2599 (class 0 OID 17714)
-- Dependencies: 181
-- Data for Name: avaliacao_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2600 (class 0 OID 17719)
-- Dependencies: 182
-- Data for Name: categoria_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2601 (class 0 OID 17724)
-- Dependencies: 183
-- Data for Name: conta_pagar; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2602 (class 0 OID 17732)
-- Dependencies: 184
-- Data for Name: conta_receber; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2603 (class 0 OID 17740)
-- Dependencies: 185
-- Data for Name: cupom_desconto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2604 (class 0 OID 17745)
-- Dependencies: 186
-- Data for Name: endereco; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2605 (class 0 OID 17753)
-- Dependencies: 187
-- Data for Name: forma_pagamento; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2606 (class 0 OID 17758)
-- Dependencies: 188
-- Data for Name: imagem_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2607 (class 0 OID 17766)
-- Dependencies: 189
-- Data for Name: item_venda_loja; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2608 (class 0 OID 17774)
-- Dependencies: 190
-- Data for Name: marca_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2609 (class 0 OID 17779)
-- Dependencies: 191
-- Data for Name: nota_fiscal_compra; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2610 (class 0 OID 17787)
-- Dependencies: 192
-- Data for Name: nota_fiscal_venda; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2611 (class 0 OID 17795)
-- Dependencies: 193
-- Data for Name: nota_item_produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2612 (class 0 OID 17800)
-- Dependencies: 194
-- Data for Name: pessoa_fisica; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2613 (class 0 OID 17808)
-- Dependencies: 195
-- Data for Name: pessoa_juridica; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2614 (class 0 OID 17816)
-- Dependencies: 196
-- Data for Name: produto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2647 (class 0 OID 0)
-- Dependencies: 220
-- Name: seq_acesso; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_acesso', 1, false);


--
-- TOC entry 2648 (class 0 OID 0)
-- Dependencies: 201
-- Name: seq_avaliacao_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_avaliacao_produto', 1, false);


--
-- TOC entry 2649 (class 0 OID 0)
-- Dependencies: 202
-- Name: seq_categoria_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_categoria_produto', 1, false);


--
-- TOC entry 2650 (class 0 OID 0)
-- Dependencies: 203
-- Name: seq_conta_pagar; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_conta_pagar', 1, false);


--
-- TOC entry 2651 (class 0 OID 0)
-- Dependencies: 204
-- Name: seq_conta_receber; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_conta_receber', 1, false);


--
-- TOC entry 2652 (class 0 OID 0)
-- Dependencies: 205
-- Name: seq_cupom_desconto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_cupom_desconto', 1, false);


--
-- TOC entry 2653 (class 0 OID 0)
-- Dependencies: 206
-- Name: seq_endereco; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_endereco', 1, false);


--
-- TOC entry 2654 (class 0 OID 0)
-- Dependencies: 207
-- Name: seq_forma_pagamento; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_forma_pagamento', 1, false);


--
-- TOC entry 2655 (class 0 OID 0)
-- Dependencies: 208
-- Name: seq_imagem_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_imagem_produto', 1, false);


--
-- TOC entry 2656 (class 0 OID 0)
-- Dependencies: 209
-- Name: seq_item_venda_loja; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_item_venda_loja', 1, false);


--
-- TOC entry 2657 (class 0 OID 0)
-- Dependencies: 210
-- Name: seq_marca_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_marca_produto', 1, false);


--
-- TOC entry 2658 (class 0 OID 0)
-- Dependencies: 211
-- Name: seq_nota_fiscal_compra; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_nota_fiscal_compra', 1, false);


--
-- TOC entry 2659 (class 0 OID 0)
-- Dependencies: 212
-- Name: seq_nota_fiscal_venda; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_nota_fiscal_venda', 1, false);


--
-- TOC entry 2660 (class 0 OID 0)
-- Dependencies: 213
-- Name: seq_nota_item_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_nota_item_produto', 1, false);


--
-- TOC entry 2661 (class 0 OID 0)
-- Dependencies: 214
-- Name: seq_pessoa; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_pessoa', 1, false);


--
-- TOC entry 2662 (class 0 OID 0)
-- Dependencies: 215
-- Name: seq_produto; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_produto', 1, false);


--
-- TOC entry 2663 (class 0 OID 0)
-- Dependencies: 216
-- Name: seq_status_rastreio; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_status_rastreio', 1, false);


--
-- TOC entry 2664 (class 0 OID 0)
-- Dependencies: 217
-- Name: seq_usuario; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_usuario', 1, false);


--
-- TOC entry 2665 (class 0 OID 0)
-- Dependencies: 218
-- Name: seq_vd_cp_loja_virt; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('seq_vd_cp_loja_virt', 1, false);


--
-- TOC entry 2615 (class 0 OID 17824)
-- Dependencies: 197
-- Data for Name: status_rastreio; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2616 (class 0 OID 17832)
-- Dependencies: 198
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2617 (class 0 OID 17840)
-- Dependencies: 199
-- Data for Name: usuarios_acesso; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2618 (class 0 OID 17843)
-- Dependencies: 200
-- Data for Name: vd_cp_loja_virt; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2422 (class 2606 OID 18072)
-- Name: acesso_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY acesso
    ADD CONSTRAINT acesso_pkey PRIMARY KEY (id);


--
-- TOC entry 2378 (class 2606 OID 17718)
-- Name: avaliacao_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY avaliacao_produto
    ADD CONSTRAINT avaliacao_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2380 (class 2606 OID 17723)
-- Name: categoria_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categoria_produto
    ADD CONSTRAINT categoria_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2382 (class 2606 OID 17731)
-- Name: conta_pagar_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conta_pagar
    ADD CONSTRAINT conta_pagar_pkey PRIMARY KEY (id);


--
-- TOC entry 2384 (class 2606 OID 17739)
-- Name: conta_receber_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conta_receber
    ADD CONSTRAINT conta_receber_pkey PRIMARY KEY (id);


--
-- TOC entry 2386 (class 2606 OID 17744)
-- Name: cupom_desconto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cupom_desconto
    ADD CONSTRAINT cupom_desconto_pkey PRIMARY KEY (id);


--
-- TOC entry 2388 (class 2606 OID 17752)
-- Name: endereco_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY endereco
    ADD CONSTRAINT endereco_pkey PRIMARY KEY (id);


--
-- TOC entry 2390 (class 2606 OID 17757)
-- Name: forma_pagamento_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY forma_pagamento
    ADD CONSTRAINT forma_pagamento_pkey PRIMARY KEY (id);


--
-- TOC entry 2392 (class 2606 OID 17765)
-- Name: imagem_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY imagem_produto
    ADD CONSTRAINT imagem_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2394 (class 2606 OID 17773)
-- Name: item_venda_loja_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_venda_loja
    ADD CONSTRAINT item_venda_loja_pkey PRIMARY KEY (id);


--
-- TOC entry 2412 (class 2606 OID 18067)
-- Name: login_unique; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT login_unique UNIQUE (login);


--
-- TOC entry 2396 (class 2606 OID 17778)
-- Name: marca_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY marca_produto
    ADD CONSTRAINT marca_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2398 (class 2606 OID 17786)
-- Name: nota_fiscal_compra_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_compra
    ADD CONSTRAINT nota_fiscal_compra_pkey PRIMARY KEY (id);


--
-- TOC entry 2400 (class 2606 OID 17794)
-- Name: nota_fiscal_venda_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_venda
    ADD CONSTRAINT nota_fiscal_venda_pkey PRIMARY KEY (id);


--
-- TOC entry 2402 (class 2606 OID 17799)
-- Name: nota_item_produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_item_produto
    ADD CONSTRAINT nota_item_produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2404 (class 2606 OID 17807)
-- Name: pessoa_fisica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pessoa_fisica
    ADD CONSTRAINT pessoa_fisica_pkey PRIMARY KEY (id);


--
-- TOC entry 2406 (class 2606 OID 17815)
-- Name: pessoa_juridica_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pessoa_juridica
    ADD CONSTRAINT pessoa_juridica_pkey PRIMARY KEY (id);


--
-- TOC entry 2408 (class 2606 OID 17823)
-- Name: produto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY produto
    ADD CONSTRAINT produto_pkey PRIMARY KEY (id);


--
-- TOC entry 2410 (class 2606 OID 17831)
-- Name: status_rastreio_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY status_rastreio
    ADD CONSTRAINT status_rastreio_pkey PRIMARY KEY (id);


--
-- TOC entry 2416 (class 2606 OID 17849)
-- Name: uk_8bak9jswon2id2jbunuqlfl9e; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_acesso
    ADD CONSTRAINT uk_8bak9jswon2id2jbunuqlfl9e UNIQUE (acesso_id);


--
-- TOC entry 2418 (class 2606 OID 17851)
-- Name: unique_acesso_user; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_acesso
    ADD CONSTRAINT unique_acesso_user UNIQUE (usuario_id, acesso_id);


--
-- TOC entry 2414 (class 2606 OID 17839)
-- Name: usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 2420 (class 2606 OID 17847)
-- Name: vd_cp_loja_virt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
    ADD CONSTRAINT vd_cp_loja_virt_pkey PRIMARY KEY (id);


--
-- TOC entry 2460 (class 2620 OID 18083)
-- Name: validachavepessoaavaliacaoprodutodelete; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaavaliacaoprodutodelete BEFORE DELETE ON public.avaliacao_produto FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2459 (class 2620 OID 18082)
-- Name: validachavepessoaavaliacaoprodutoinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaavaliacaoprodutoinsert BEFORE INSERT ON public.avaliacao_produto FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2458 (class 2620 OID 18081)
-- Name: validachavepessoaavaliacaoprodutoupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaavaliacaoprodutoupdate BEFORE UPDATE ON public.avaliacao_produto FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2463 (class 2620 OID 18087)
-- Name: validachavepessoacategoriaprodutodelete; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacategoriaprodutodelete BEFORE DELETE ON public.categoria_produto FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2462 (class 2620 OID 18086)
-- Name: validachavepessoacategoriaprodutoinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacategoriaprodutoinsert BEFORE INSERT ON public.categoria_produto FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2461 (class 2620 OID 18085)
-- Name: validachavepessoacategoriaprodutoupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacategoriaprodutoupdate BEFORE UPDATE ON public.categoria_produto FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2469 (class 2620 OID 18095)
-- Name: validachavepessoacontapagar_forndelete; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontapagar_forndelete BEFORE DELETE ON public.conta_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoa_forn();


--
-- TOC entry 2468 (class 2620 OID 18094)
-- Name: validachavepessoacontapagar_forninsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontapagar_forninsert BEFORE INSERT ON public.conta_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoa_forn();


--
-- TOC entry 2467 (class 2620 OID 18093)
-- Name: validachavepessoacontapagar_fornupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontapagar_fornupdate BEFORE UPDATE ON public.conta_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoa_forn();


--
-- TOC entry 2466 (class 2620 OID 18091)
-- Name: validachavepessoacontapagardelete; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontapagardelete BEFORE DELETE ON public.conta_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2465 (class 2620 OID 18090)
-- Name: validachavepessoacontapagarinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontapagarinsert BEFORE INSERT ON public.conta_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2464 (class 2620 OID 18089)
-- Name: validachavepessoacontapagarupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontapagarupdate BEFORE UPDATE ON public.conta_pagar FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2472 (class 2620 OID 18098)
-- Name: validachavepessoacontareceberdelete; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontareceberdelete BEFORE DELETE ON public.conta_receber FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2471 (class 2620 OID 18097)
-- Name: validachavepessoacontareceberinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontareceberinsert BEFORE INSERT ON public.conta_receber FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2470 (class 2620 OID 18096)
-- Name: validachavepessoacontareceberupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoacontareceberupdate BEFORE UPDATE ON public.conta_receber FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2475 (class 2620 OID 18101)
-- Name: validachavepessoaenderecodelete; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaenderecodelete BEFORE DELETE ON public.endereco FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2474 (class 2620 OID 18100)
-- Name: validachavepessoaenderecoinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaenderecoinsert BEFORE INSERT ON public.endereco FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2473 (class 2620 OID 18099)
-- Name: validachavepessoaenderecoupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoaenderecoupdate BEFORE UPDATE ON public.endereco FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2478 (class 2620 OID 18104)
-- Name: validachavepessoanotafiscalcompradelete; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoanotafiscalcompradelete BEFORE DELETE ON public.nota_fiscal_compra FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2477 (class 2620 OID 18103)
-- Name: validachavepessoanotafiscalcomprainsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoanotafiscalcomprainsert BEFORE INSERT ON public.nota_fiscal_compra FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2476 (class 2620 OID 18102)
-- Name: validachavepessoanotafiscalcompraupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoanotafiscalcompraupdate BEFORE UPDATE ON public.nota_fiscal_compra FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2481 (class 2620 OID 18107)
-- Name: validachavepessoausuariodelete; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoausuariodelete BEFORE DELETE ON public.usuario FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2480 (class 2620 OID 18106)
-- Name: validachavepessoausuarioinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoausuarioinsert BEFORE INSERT ON public.usuario FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2479 (class 2620 OID 18105)
-- Name: validachavepessoausuarioupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoausuarioupdate BEFORE UPDATE ON public.usuario FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2484 (class 2620 OID 18110)
-- Name: validachavepessoavdcplojavirtdelete; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoavdcplojavirtdelete BEFORE DELETE ON public.vd_cp_loja_virt FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2483 (class 2620 OID 18109)
-- Name: validachavepessoavdcplojavirtinsert; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoavdcplojavirtinsert BEFORE INSERT ON public.vd_cp_loja_virt FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2482 (class 2620 OID 18108)
-- Name: validachavepessoavdcplojavirtupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER validachavepessoavdcplojavirtupdate BEFORE UPDATE ON public.vd_cp_loja_virt FOR EACH ROW EXECUTE PROCEDURE validachavepessoa();


--
-- TOC entry 2451 (class 2606 OID 18075)
-- Name: acesso_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_acesso
    ADD CONSTRAINT acesso_fk FOREIGN KEY (acesso_id) REFERENCES acesso(id);


--
-- TOC entry 2436 (class 2606 OID 17905)
-- Name: conta_pagar_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_compra
    ADD CONSTRAINT conta_pagar_fk FOREIGN KEY (conta_pagar_id) REFERENCES conta_pagar(id);


--
-- TOC entry 2452 (class 2606 OID 17945)
-- Name: cupom_desconto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
    ADD CONSTRAINT cupom_desconto_fk FOREIGN KEY (cupom_desconto_id) REFERENCES cupom_desconto(id);


--
-- TOC entry 2424 (class 2606 OID 17971)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY avaliacao_produto
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2425 (class 2606 OID 17976)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categoria_produto
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2426 (class 2606 OID 17981)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conta_pagar
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2427 (class 2606 OID 17986)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conta_receber
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2428 (class 2606 OID 17991)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cupom_desconto
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2429 (class 2606 OID 17996)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY endereco
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2430 (class 2606 OID 18001)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY forma_pagamento
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2432 (class 2606 OID 18006)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY imagem_produto
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2434 (class 2606 OID 18011)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_venda_loja
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2435 (class 2606 OID 18016)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY marca_produto
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2437 (class 2606 OID 18021)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_compra
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2439 (class 2606 OID 18026)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_venda
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2442 (class 2606 OID 18031)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_item_produto
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2443 (class 2606 OID 18036)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pessoa_fisica
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2444 (class 2606 OID 18041)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY pessoa_juridica
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2446 (class 2606 OID 18046)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY produto
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2448 (class 2606 OID 18051)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY status_rastreio
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2449 (class 2606 OID 18056)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuario
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2457 (class 2606 OID 18061)
-- Name: empresa_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
    ADD CONSTRAINT empresa_fk FOREIGN KEY (empresa_id) REFERENCES pessoa_juridica(id);


--
-- TOC entry 2453 (class 2606 OID 17950)
-- Name: endereco_cobranca_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
    ADD CONSTRAINT endereco_cobranca_fk FOREIGN KEY (endereco_cobranca_id) REFERENCES endereco(id);


--
-- TOC entry 2454 (class 2606 OID 17955)
-- Name: endereco_entrega_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
    ADD CONSTRAINT endereco_entrega_fk FOREIGN KEY (endereco_entrega_id) REFERENCES endereco(id);


--
-- TOC entry 2455 (class 2606 OID 17960)
-- Name: forma_pagamento_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
    ADD CONSTRAINT forma_pagamento_fk FOREIGN KEY (forma_pagamento_id) REFERENCES forma_pagamento(id);


--
-- TOC entry 2440 (class 2606 OID 17915)
-- Name: nota_fiscal_compra_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_item_produto
    ADD CONSTRAINT nota_fiscal_compra_fk FOREIGN KEY (nota_fiscal_compra_id) REFERENCES nota_fiscal_compra(id);


--
-- TOC entry 2456 (class 2606 OID 17965)
-- Name: nota_fiscal_venda_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY vd_cp_loja_virt
    ADD CONSTRAINT nota_fiscal_venda_fk FOREIGN KEY (nota_fiscal_venda_id) REFERENCES nota_fiscal_venda(id);


--
-- TOC entry 2445 (class 2606 OID 17925)
-- Name: nota_item_produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY produto
    ADD CONSTRAINT nota_item_produto_fk FOREIGN KEY (nota_item_produto_id) REFERENCES nota_item_produto(id);


--
-- TOC entry 2423 (class 2606 OID 17890)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY avaliacao_produto
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES produto(id);


--
-- TOC entry 2431 (class 2606 OID 17895)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY imagem_produto
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES produto(id);


--
-- TOC entry 2433 (class 2606 OID 17900)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY item_venda_loja
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES produto(id);


--
-- TOC entry 2441 (class 2606 OID 17920)
-- Name: produto_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_item_produto
    ADD CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES produto(id);


--
-- TOC entry 2450 (class 2606 OID 17940)
-- Name: usuario_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY usuarios_acesso
    ADD CONSTRAINT usuario_fk FOREIGN KEY (usuario_id) REFERENCES usuario(id);


--
-- TOC entry 2438 (class 2606 OID 17910)
-- Name: venda_compra_loja_virt_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nota_fiscal_venda
    ADD CONSTRAINT venda_compra_loja_virt_fk FOREIGN KEY (venda_compra_loja_virt_id) REFERENCES vd_cp_loja_virt(id);


--
-- TOC entry 2447 (class 2606 OID 17930)
-- Name: venda_compra_loja_virt_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY status_rastreio
    ADD CONSTRAINT venda_compra_loja_virt_fk FOREIGN KEY (venda_compra_loja_virt_id) REFERENCES vd_cp_loja_virt(id);


--
-- TOC entry 2645 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2022-06-02 03:45:00 -03

--
-- PostgreSQL database dump complete
--


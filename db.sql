--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- Name: account_ids; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE account_ids
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.account_ids OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: account; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE account (
    id integer DEFAULT nextval('account_ids'::regclass) NOT NULL,
    name character(64) NOT NULL,
    notes character(128)
);


ALTER TABLE public.account OWNER TO postgres;

--
-- Name: agent_ids; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE agent_ids
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.agent_ids OWNER TO postgres;

--
-- Name: agent; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE agent (
    id integer DEFAULT nextval('agent_ids'::regclass) NOT NULL,
    name character(64) NOT NULL,
    agent_type integer NOT NULL,
    notes character(128)
);


ALTER TABLE public.agent OWNER TO postgres;

--
-- Name: agent_type; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE agent_type (
    id integer DEFAULT nextval('account_ids'::regclass) NOT NULL,
    name character(64) NOT NULL
);


ALTER TABLE public.agent_type OWNER TO postgres;

--
-- Name: agent_type_ids; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE agent_type_ids
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.agent_type_ids OWNER TO postgres;

--
-- Name: document_type_ids; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE document_type_ids
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.document_type_ids OWNER TO postgres;

--
-- Name: document_type; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE document_type (
    id integer DEFAULT nextval('document_type_ids'::regclass) NOT NULL,
    name character(64) NOT NULL,
    agent_type integer NOT NULL,
    description character(128)
);


ALTER TABLE public.document_type OWNER TO postgres;

--
-- Name: nomenclature_ids; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE nomenclature_ids
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.nomenclature_ids OWNER TO postgres;

--
-- Name: nomenclature; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE nomenclature (
    id integer DEFAULT nextval('nomenclature_ids'::regclass) NOT NULL,
    name character(64) NOT NULL,
    purchase_price integer NOT NULL,
    sale_price integer NOT NULL,
    rate character(64),
    notes character(128)
);


ALTER TABLE public.nomenclature OWNER TO postgres;

--
-- Name: operation_log_ids; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE operation_log_ids
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.operation_log_ids OWNER TO postgres;

--
-- Name: operation_log; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE operation_log (
    id integer DEFAULT nextval('operation_log_ids'::regclass) NOT NULL,
    data timestamp without time zone NOT NULL,
    price integer NOT NULL,
    quantity integer NOT NULL,
    total integer NOT NULL,
    document_type integer NOT NULL,
    agent_id integer NOT NULL,
    nomenclature_id integer NOT NULL
);


ALTER TABLE public.operation_log OWNER TO postgres;

--
-- Name: storage; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE storage (
    nomenclature_id integer NOT NULL,
    quantity integer NOT NULL,
    total integer NOT NULL,
    avg_price integer
);


ALTER TABLE public.storage OWNER TO postgres;

--
-- Name: transaction_log_ids; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE transaction_log_ids
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.transaction_log_ids OWNER TO postgres;

--
-- Name: transaction_log; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE transaction_log (
    id integer DEFAULT nextval('transaction_log_ids'::regclass) NOT NULL,
    transaction_type integer NOT NULL,
    operation_id integer NOT NULL,
    total integer NOT NULL
);


ALTER TABLE public.transaction_log OWNER TO postgres;

--
-- Name: transaction_type_ids; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE transaction_type_ids
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.transaction_type_ids OWNER TO postgres;

--
-- Name: transaction_type; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE transaction_type (
    id integer DEFAULT nextval('transaction_type_ids'::regclass) NOT NULL,
    document_type integer NOT NULL,
    account_credit integer NOT NULL,
    account_debit integer NOT NULL
);


ALTER TABLE public.transaction_type OWNER TO postgres;

--
-- Name: turn_sheets; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE turn_sheets (
    account_id integer NOT NULL,
    turn_credit integer NOT NULL,
    turn_debit integer NOT NULL,
    saldo_beg_credit integer NOT NULL,
    saldo_beg_debit integer NOT NULL,
    saldo_fin_credit integer,
    saldo_fin_debit integer
);


ALTER TABLE public.turn_sheets OWNER TO postgres;

--
-- Data for Name: account; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY account (id, name, notes) FROM stdin;
6	281                                                             	Товары на складе                                                                                                                
7	311                                                             	Расчетный счет у национальной валюте                                                                                            
8	361                                                             	Расчет с отечественными покупателями                                                                                            
9	631                                                             	Расчет с отечественными поставщиками                                                                                            
10	702                                                             	Доход от реализации товаров                                                                                                     
11	902                                                             	Себестоимость реализованых товаров                                                                                              
\.


--
-- Name: account_ids; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('account_ids', 11, true);


--
-- Data for Name: agent; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY agent (id, name, agent_type, notes) FROM stdin;
1	Васильев Николай Владимерович                                   	2	\N
2	Петров Василий Николаевич                                       	2	\N
4	Козак Олесь Сергеевич                                           	1	\N
3	Луканина Наталия Тимофеевна                                     	1	12356513                                                                                                                        
\.


--
-- Name: agent_ids; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('agent_ids', 19, true);


--
-- Data for Name: agent_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY agent_type (id, name) FROM stdin;
1	Поставщик                                                       
2	Покупатель                                                      
\.


--
-- Name: agent_type_ids; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('agent_type_ids', 1, false);


--
-- Data for Name: document_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY document_type (id, name, agent_type, description) FROM stdin;
1	ПН                                                              	1	Приходная накладная                                                                                                             
2	РН                                                              	2	Расходная накладная                                                                                                             
3	БВП                                                             	2	Банковская выписка прибыли                                                                                                      
4	БВР                                                             	1	Банковская выписка расходов                                                                                                     
\.


--
-- Name: document_type_ids; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('document_type_ids', 4, true);


--
-- Data for Name: nomenclature; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY nomenclature (id, name, purchase_price, sale_price, rate, notes) FROM stdin;
1	Товар 1                                                         	120	150	Хорошее                                                         	Барабашово                                                                                                                      
2	товар 123                                                       	10	25	Отличное                                                        	                                                                                                                                
\.


--
-- Name: nomenclature_ids; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('nomenclature_ids', 2, true);


--
-- Data for Name: operation_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY operation_log (id, data, price, quantity, total, document_type, agent_id, nomenclature_id) FROM stdin;
2	2014-12-14 00:00:00	15	1	15	1	4	1
\.


--
-- Name: operation_log_ids; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('operation_log_ids', 4, true);


--
-- Data for Name: storage; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY storage (nomenclature_id, quantity, total, avg_price) FROM stdin;
1	1	15	15
2	0	0	0
\.


--
-- Data for Name: transaction_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY transaction_log (id, transaction_type, operation_id, total) FROM stdin;
1	1	2	15
\.


--
-- Name: transaction_log_ids; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('transaction_log_ids', 4, true);


--
-- Data for Name: transaction_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY transaction_type (id, document_type, account_credit, account_debit) FROM stdin;
1	1	9	6
2	2	10	8
3	2	6	11
4	3	8	7
5	4	7	9
\.


--
-- Name: transaction_type_ids; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('transaction_type_ids', 5, true);


--
-- Data for Name: turn_sheets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY turn_sheets (account_id, turn_credit, turn_debit, saldo_beg_credit, saldo_beg_debit, saldo_fin_credit, saldo_fin_debit) FROM stdin;
6	0	15	0	15	0	30
7	0	0	0	0	0	0
8	0	0	0	0	0	0
9	15	0	15	0	30	0
10	0	0	0	0	0	0
11	0	0	0	0	0	0
\.


--
-- Name: account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- Name: agent_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY agent
    ADD CONSTRAINT agent_pkey PRIMARY KEY (id);


--
-- Name: agent_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY agent_type
    ADD CONSTRAINT agent_type_pkey PRIMARY KEY (id);


--
-- Name: document_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY document_type
    ADD CONSTRAINT document_type_pkey PRIMARY KEY (id);


--
-- Name: nomenclature_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY nomenclature
    ADD CONSTRAINT nomenclature_pkey PRIMARY KEY (id);


--
-- Name: operation_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY operation_log
    ADD CONSTRAINT operation_log_pkey PRIMARY KEY (id);


--
-- Name: storage_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY storage
    ADD CONSTRAINT storage_pkey PRIMARY KEY (nomenclature_id);


--
-- Name: transaction_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY transaction_log
    ADD CONSTRAINT transaction_log_pkey PRIMARY KEY (id);


--
-- Name: transaction_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY transaction_type
    ADD CONSTRAINT transaction_type_pkey PRIMARY KEY (id);


--
-- Name: turn_sheets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY turn_sheets
    ADD CONSTRAINT turn_sheets_pkey PRIMARY KEY (account_id);


--
-- Name: agentagent_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY agent
    ADD CONSTRAINT agentagent_type FOREIGN KEY (agent_type) REFERENCES agent_type(id);


--
-- Name: document_typeagent_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY document_type
    ADD CONSTRAINT document_typeagent_type FOREIGN KEY (agent_type) REFERENCES agent_type(id);


--
-- Name: operation_logagent; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY operation_log
    ADD CONSTRAINT operation_logagent FOREIGN KEY (agent_id) REFERENCES agent(id);


--
-- Name: operation_logdocument_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY operation_log
    ADD CONSTRAINT operation_logdocument_type FOREIGN KEY (document_type) REFERENCES document_type(id);


--
-- Name: operation_lognomenclature; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY operation_log
    ADD CONSTRAINT operation_lognomenclature FOREIGN KEY (nomenclature_id) REFERENCES nomenclature(id);


--
-- Name: storagenomenclature; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY storage
    ADD CONSTRAINT storagenomenclature FOREIGN KEY (nomenclature_id) REFERENCES nomenclature(id);


--
-- Name: transaction_logoperation_log; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY transaction_log
    ADD CONSTRAINT transaction_logoperation_log FOREIGN KEY (operation_id) REFERENCES operation_log(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: transaction_logtransaction_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY transaction_log
    ADD CONSTRAINT transaction_logtransaction_type FOREIGN KEY (transaction_type) REFERENCES transaction_type(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: transaction_typeaccountcredit; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY transaction_type
    ADD CONSTRAINT transaction_typeaccountcredit FOREIGN KEY (account_credit) REFERENCES account(id);


--
-- Name: transaction_typeaccountdebit; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY transaction_type
    ADD CONSTRAINT transaction_typeaccountdebit FOREIGN KEY (account_debit) REFERENCES account(id);


--
-- Name: transaction_typedocument_type; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY transaction_type
    ADD CONSTRAINT transaction_typedocument_type FOREIGN KEY (document_type) REFERENCES document_type(id);


--
-- Name: turn_sheetsaccount; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY turn_sheets
    ADD CONSTRAINT turn_sheetsaccount FOREIGN KEY (account_id) REFERENCES account(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--


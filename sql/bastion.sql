--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: bastion; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA bastion;


ALTER SCHEMA bastion OWNER TO postgres;

SET search_path = bastion, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: roles; Type: TABLE; Schema: bastion; Owner: postgres; Tablespace: 
--

CREATE TABLE roles (
    "user" integer NOT NULL,
    role character varying(255) NOT NULL
);


ALTER TABLE bastion.roles OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: bastion; Owner: postgres; Tablespace: 
--

CREATE TABLE users (
    id integer NOT NULL,
    username character varying(255),
    password character(32),
    blocked boolean DEFAULT false NOT NULL
);


ALTER TABLE bastion.users OWNER TO postgres;

--
-- Name: groups_for_iconcerto_realm; Type: VIEW; Schema: bastion; Owner: postgres
--

CREATE VIEW groups_for_iconcerto_realm AS
    SELECT u.username, r.role FROM (users u RIGHT JOIN roles r ON ((u.id = r."user")));


ALTER TABLE bastion.groups_for_iconcerto_realm OWNER TO postgres;

--
-- Name: meta_keys; Type: TABLE; Schema: bastion; Owner: postgres; Tablespace: 
--

CREATE TABLE meta_keys (
    id integer NOT NULL,
    name character varying(256),
    description character varying(1024)
);


ALTER TABLE bastion.meta_keys OWNER TO postgres;

--
-- Name: meta_keys_id_seq; Type: SEQUENCE; Schema: bastion; Owner: postgres
--

CREATE SEQUENCE meta_keys_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bastion.meta_keys_id_seq OWNER TO postgres;

--
-- Name: meta_keys_id_seq; Type: SEQUENCE OWNED BY; Schema: bastion; Owner: postgres
--

ALTER SEQUENCE meta_keys_id_seq OWNED BY meta_keys.id;


--
-- Name: meta_values; Type: TABLE; Schema: bastion; Owner: postgres; Tablespace: 
--

CREATE TABLE meta_values (
    id integer NOT NULL,
    meta_key integer NOT NULL,
    "user" integer NOT NULL,
    value character varying(4096)
);


ALTER TABLE bastion.meta_values OWNER TO postgres;

--
-- Name: meta_values_id_seq; Type: SEQUENCE; Schema: bastion; Owner: postgres
--

CREATE SEQUENCE meta_values_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bastion.meta_values_id_seq OWNER TO postgres;

--
-- Name: meta_values_id_seq; Type: SEQUENCE OWNED BY; Schema: bastion; Owner: postgres
--

ALTER SEQUENCE meta_values_id_seq OWNED BY meta_values.id;


--
-- Name: users_for_iconcerto_realm; Type: VIEW; Schema: bastion; Owner: postgres
--

CREATE VIEW users_for_iconcerto_realm AS
    SELECT users.username, users.password FROM users;


ALTER TABLE bastion.users_for_iconcerto_realm OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: bastion; Owner: postgres
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bastion.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: bastion; Owner: postgres
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: id; Type: DEFAULT; Schema: bastion; Owner: postgres
--

ALTER TABLE meta_keys ALTER COLUMN id SET DEFAULT nextval('meta_keys_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: bastion; Owner: postgres
--

ALTER TABLE meta_values ALTER COLUMN id SET DEFAULT nextval('meta_values_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: bastion; Owner: postgres
--

ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Name: meta_keys_pkey; Type: CONSTRAINT; Schema: bastion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY meta_keys
    ADD CONSTRAINT meta_keys_pkey PRIMARY KEY (id);


--
-- Name: meta_values_pkey; Type: CONSTRAINT; Schema: bastion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY meta_values
    ADD CONSTRAINT meta_values_pkey PRIMARY KEY (id);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: bastion; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: meta_values_meta_key_fkey; Type: FK CONSTRAINT; Schema: bastion; Owner: postgres
--

ALTER TABLE ONLY meta_values
    ADD CONSTRAINT meta_values_meta_key_fkey FOREIGN KEY (meta_key) REFERENCES meta_keys(id);


--
-- Name: meta_values_user_fkey; Type: FK CONSTRAINT; Schema: bastion; Owner: postgres
--

ALTER TABLE ONLY meta_values
    ADD CONSTRAINT meta_values_user_fkey FOREIGN KEY ("user") REFERENCES users(id);


--
-- Name: roles_user_fkey; Type: FK CONSTRAINT; Schema: bastion; Owner: postgres
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_user_fkey FOREIGN KEY ("user") REFERENCES users(id);


--
-- PostgreSQL database dump complete
--


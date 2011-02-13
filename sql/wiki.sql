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
-- Name: wiki; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA wiki;


ALTER SCHEMA wiki OWNER TO postgres;

SET search_path = wiki, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: pages; Type: TABLE; Schema: wiki; Owner: postgres; Tablespace: 
--

CREATE TABLE pages (
    name character varying(512) NOT NULL
);


ALTER TABLE wiki.pages OWNER TO postgres;

--
-- Name: revisions; Type: TABLE; Schema: wiki; Owner: postgres; Tablespace: 
--

CREATE TABLE revisions (
    revision character(36) NOT NULL,
    page character varying(512),
    "timestamp" timestamp without time zone DEFAULT now() NOT NULL,
    code text NOT NULL,
    xhtml text NOT NULL
);


ALTER TABLE wiki.revisions OWNER TO postgres;

--
-- Name: pages_pkey; Type: CONSTRAINT; Schema: wiki; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY pages
    ADD CONSTRAINT pages_pkey PRIMARY KEY (name);


--
-- Name: revisions_pkey; Type: CONSTRAINT; Schema: wiki; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY revisions
    ADD CONSTRAINT revisions_pkey PRIMARY KEY (revision);


--
-- Name: revisions_page_fkey; Type: FK CONSTRAINT; Schema: wiki; Owner: postgres
--

ALTER TABLE ONLY revisions
    ADD CONSTRAINT revisions_page_fkey FOREIGN KEY (page) REFERENCES pages(name);


--
-- PostgreSQL database dump complete
--


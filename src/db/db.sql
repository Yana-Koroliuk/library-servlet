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

-- Name: order_status; Type: TYPE; Schema: public; Owner: library
CREATE TYPE public.order_status AS ENUM ('READER_HOLE', 'RECEIVED', 'APPROVED', 'RETURNED', 'OVERDUE', 'CANCELED');
ALTER TYPE public.order_status OWNER TO library;

-- Name: role; Type: TYPE; Schema: public; Owner: library
CREATE TYPE public.role AS ENUM ('ADMIN', 'LIBRARIAN','READER');
ALTER TYPE public.role OWNER TO library;

SET default_tablespace = '';
SET default_table_access_method = heap;

-- Name: users; Type: TABLE; Schema: public; Owner: library
CREATE TABLE public.users (
    id bigint NOT NULL,
    login character varying(20) NOT NULL,
    password_hash character varying(100) NOT NULL,
    user_role public.role NOT NULL,
    is_blocked boolean NOT NULL
);
ALTER TABLE public.users OWNER TO library;

-- Name: edition; Type: TABLE; Schema: public; Owner: library
CREATE TABLE public.edition (
    id bigint NOT NULL,
    edition_name_uk character varying(100) NOT NULL,
    edition_name_en character varying(100) NOT NULL
);
ALTER TABLE public.edition OWNER TO library;

-- Name: book; Type: TABLE; Schema: public; Owner: library
CREATE TABLE public.book (
    id bigint NOT NULL,
    title_uk character varying(100) NOT NULL,
    description_uk character varying(1000) NOT NULL,
    edition_id bigint NOT NULL,
    language_uk character varying(20) NOT NULL,
    publication_date date NOT NULL,
    price_uan real NOT NULL,
    count bigint NOT NULL,
    title_en character varying(100) NOT NULL,
    description_en character varying(1000) NOT NULL,
    language_en character varying(20) NOT NULL
);
ALTER TABLE public.book OWNER TO library;

-- Name: author; Type: TABLE; Schema: public; Owner: library
CREATE TABLE public.author (
    id bigint NOT NULL,
    full_name_uk character varying(50) NOT NULL,
    full_name_en character varying(50) NOT NULL
);
ALTER TABLE public.author OWNER TO library;

-- Name: authorship; Type: TABLE; Schema: public; Owner: library
CREATE TABLE public.authorship (
    id bigint NOT NULL,
    author_id bigint NOT NULL,
    book_id bigint NOT NULL
);
ALTER TABLE public.authorship OWNER TO library;

-- Name: orders; Type: TABLE; Schema: public; Owner: library
CREATE TABLE public.orders (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    book_id bigint NOT NULL,
    start_date date,
    end_date date,
    status public.order_status NOT NULL
);
ALTER TABLE public.orders OWNER TO library;

-- Name: author_id_seq; Type: SEQUENCE; Schema: public; Owner: library
ALTER TABLE public.author ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (SEQUENCE NAME public.author_id_seq START
    WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1);

-- Name: authorship_id_seq; Type: SEQUENCE; Schema: public; Owner: library
ALTER TABLE public.authorship ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (SEQUENCE NAME public.authorship_id_seq
    START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1);

-- Name: book_id_seq; Type: SEQUENCE; Schema: public; Owner: library
ALTER TABLE public.book ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (SEQUENCE NAME public.book_id_seq START WITH 1
    INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1);

-- Name: edition_id_seq; Type: SEQUENCE; Schema: public; Owner: library
ALTER TABLE public.edition ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (SEQUENCE NAME public.edition_id_seq
    START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1);

-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: library
ALTER TABLE public.orders ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (SEQUENCE NAME public.orders_id_seq
    START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1);

-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: library
ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (SEQUENCE NAME public.users_id_seq START WITH 1
    INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1);

-- Name: author author_full_name_key; Type: CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.author ADD CONSTRAINT author_full_name_key UNIQUE (full_name_uk);

-- Name: author author_pkey; Type: CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.author ADD CONSTRAINT author_pkey PRIMARY KEY (id);

-- Name: authorship authorship_pkey; Type: CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.authorship ADD CONSTRAINT authorship_pkey PRIMARY KEY (id);

-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.book ADD CONSTRAINT book_pkey PRIMARY KEY (id);

-- Name: edition edition_edition_name_key; Type: CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.edition ADD CONSTRAINT edition_edition_name_key UNIQUE (edition_name_uk);

-- Name: edition edition_pkey; Type: CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.edition ADD CONSTRAINT edition_pkey PRIMARY KEY (id);

-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.orders ADD CONSTRAINT orders_pkey PRIMARY KEY (id);

-- Name: users users_login_key; Type: CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.users ADD CONSTRAINT users_login_key UNIQUE (login);

-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.users ADD CONSTRAINT users_pkey PRIMARY KEY (id);

-- Name: authorship author_id; Type: FK CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.authorship
    ADD CONSTRAINT author_id FOREIGN KEY (author_id) REFERENCES public.author(id) ON DELETE CASCADE NOT VALID;

-- Name: authorship book_id; Type: FK CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.authorship
    ADD CONSTRAINT book_id FOREIGN KEY (book_id) REFERENCES public.book(id) ON DELETE CASCADE NOT VALID;

-- Name: orders book_id; Type: FK CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.orders
    ADD CONSTRAINT book_id FOREIGN KEY (book_id) REFERENCES public.book(id) ON DELETE CASCADE NOT VALID;

-- Name: book edition_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.book
    ADD CONSTRAINT edition_id_fkey FOREIGN KEY (edition_id) REFERENCES public.edition(id) NOT VALID;

-- Name: orders user_id; Type: FK CONSTRAINT; Schema: public; Owner: library
ALTER TABLE ONLY public.orders
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE NOT VALID;

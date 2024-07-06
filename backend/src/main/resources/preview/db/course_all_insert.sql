--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0 (Debian 16.0-1.pgdg120+1)
-- Dumped by pg_dump version 16.0 (Debian 16.0-1.pgdg120+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', 'public', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

DROP TABLE IF EXISTS public.acl_entry;
DROP TABLE IF EXISTS public.acl_object_identity;
DROP TABLE IF EXISTS public.acl_class;
DROP TABLE IF EXISTS public.acl_sid;
DROP TABLE IF EXISTS public.material_tag;
DROP TABLE IF EXISTS public.user_favorites;
DROP TABLE IF EXISTS public.votes;
DROP TABLE IF EXISTS public.comment;
DROP TABLE IF EXISTS public.file;
DROP TABLE IF EXISTS public.material;
DROP TABLE IF EXISTS public.material_group;
DROP TABLE IF EXISTS public.tag;
DROP TABLE IF EXISTS public.course_year;
DROP TABLE IF EXISTS public.course;
DROP TABLE IF EXISTS public.program;
DROP TABLE IF EXISTS public.college;
DROP TABLE IF EXISTS public.user_role;
DROP TABLE IF EXISTS public.role;
DROP TABLE IF EXISTS public.users;


--
-- Name: acl_class; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.acl_class (
                                id bigint NOT NULL,
                                class character varying(100) NOT NULL
);


ALTER TABLE public.acl_class OWNER TO root;

--
-- Name: acl_class_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.acl_class_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;


ALTER SEQUENCE public.acl_class_id_seq OWNER TO root;

--
-- Name: acl_class_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.acl_class_id_seq OWNED BY public.acl_class.id;


--
-- Name: acl_entry; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.acl_entry (
                                id bigint NOT NULL,
                                acl_object_identity bigint NOT NULL,
                                ace_order integer NOT NULL,
                                sid bigint NOT NULL,
                                mask integer NOT NULL,
                                granting boolean NOT NULL,
                                audit_success boolean NOT NULL,
                                audit_failure boolean NOT NULL
);


ALTER TABLE public.acl_entry OWNER TO root;

--
-- Name: acl_entry_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.acl_entry_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;


ALTER SEQUENCE public.acl_entry_id_seq OWNER TO root;

--
-- Name: acl_entry_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.acl_entry_id_seq OWNED BY public.acl_entry.id;


--
-- Name: acl_object_identity; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.acl_object_identity (
                                          id bigint NOT NULL,
                                          object_id_class bigint NOT NULL,
                                          object_id_identity character varying(36) NOT NULL,
                                          parent_object bigint,
                                          owner_sid bigint,
                                          entries_inheriting boolean NOT NULL
);


ALTER TABLE public.acl_object_identity OWNER TO root;

--
-- Name: acl_object_identity_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.acl_object_identity_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;


ALTER SEQUENCE public.acl_object_identity_id_seq OWNER TO root;

--
-- Name: acl_object_identity_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.acl_object_identity_id_seq OWNED BY public.acl_object_identity.id;


--
-- Name: acl_sid; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.acl_sid (
                              id bigint NOT NULL,
                              principal boolean NOT NULL,
                              sid character varying(100) NOT NULL
);


ALTER TABLE public.acl_sid OWNER TO root;

--
-- Name: acl_sid_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

CREATE SEQUENCE public.acl_sid_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1;


ALTER SEQUENCE public.acl_sid_id_seq OWNER TO root;

--
-- Name: acl_sid_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: root
--

ALTER SEQUENCE public.acl_sid_id_seq OWNED BY public.acl_sid.id;


--
-- Name: college; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.college (
                              id bigint NOT NULL,
                              acronym character varying(32),
                              address character varying(128) NOT NULL,
                              city character varying(64) NOT NULL,
                              created_at timestamp(6) without time zone,
                              description character varying(512),
                              name character varying(128) NOT NULL,
                              postcode integer NOT NULL,
                              updated_at timestamp(6) without time zone,
                              website character varying(128)
);


ALTER TABLE public.college OWNER TO root;

--
-- Name: college_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.college ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
  SEQUENCE NAME public.college_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  );


--
-- Name: comment; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.comment (
                              id bigint NOT NULL,
                              created_at timestamp(6) without time zone,
                              edited boolean NOT NULL,
                              text character varying(1024) NOT NULL,
                              updated_at timestamp(6) without time zone,
                              material_id bigint NOT NULL,
                              user_id bigint NOT NULL
);


ALTER TABLE public.comment OWNER TO root;

--
-- Name: comment_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.comment ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
  SEQUENCE NAME public.comment_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  );


--
-- Name: course; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.course (
                             id bigint NOT NULL,
                             acronym character varying(32),
                             created_at timestamp(6) without time zone,
                             credits smallint NOT NULL,
                             description character varying(512),
                             name character varying(128) NOT NULL,
                             updated_at timestamp(6) without time zone,
                             year smallint,
                             program_id bigint NOT NULL
);


ALTER TABLE public.course OWNER TO root;

--
-- Name: course_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.course ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
  SEQUENCE NAME public.course_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  );


--
-- Name: course_year; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.course_year (
                                  id bigint NOT NULL,
                                  academic_year character varying(9) NOT NULL,
                                  assistant character varying(255),
                                  created_at timestamp(6) without time zone,
                                  difficulty smallint,
                                  enrollment_count smallint,
                                  exercise_count smallint,
                                  laboratory_count smallint,
                                  lecture_count smallint,
                                  passed_count smallint,
                                  professor character varying(255),
                                  updated_at timestamp(6) without time zone,
                                  course_id bigint NOT NULL
);


ALTER TABLE public.course_year OWNER TO root;

--
-- Name: course_year_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.course_year ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
  SEQUENCE NAME public.course_year_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  );


--
-- Name: file; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.file (
                           id bigint NOT NULL,
                           created_at timestamp(6) without time zone,
                           mime_type character varying(255) NOT NULL,
                           name character varying(255) NOT NULL,
                           path character varying(255) NOT NULL,
                           updated_at timestamp(6) without time zone,
                           material_id bigint NOT NULL
);


ALTER TABLE public.file OWNER TO root;

--
-- Name: file_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.file ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
  SEQUENCE NAME public.file_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  );


--
-- Name: material; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.material (
                               id bigint NOT NULL,
                               created_at timestamp(6) without time zone,
                               description character varying(512),
                               name character varying(64) NOT NULL,
                               updated_at timestamp(6) without time zone,
                               vote_count integer,
                               material_group_id bigint NOT NULL
);


ALTER TABLE public.material OWNER TO root;

--
-- Name: material_group; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.material_group (
                                     id bigint NOT NULL,
                                     created_at timestamp(6) without time zone,
                                     description character varying(512),
                                     display_order smallint NOT NULL,
                                     name character varying(64) NOT NULL,
                                     updated_at timestamp(6) without time zone,
                                     course_year_id bigint NOT NULL
);


ALTER TABLE public.material_group OWNER TO root;

--
-- Name: material_group_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.material_group ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
  SEQUENCE NAME public.material_group_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  );


--
-- Name: material_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.material ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
  SEQUENCE NAME public.material_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  );


--
-- Name: material_tag; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.material_tag (
                                   material_id bigint NOT NULL,
                                   tag_id bigint NOT NULL
);


ALTER TABLE public.material_tag OWNER TO root;

--
-- Name: program; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.program (
                              id bigint NOT NULL,
                              created_at timestamp(6) without time zone,
                              degree_title character varying(128),
                              degree_title_abrev character varying(64),
                              degree_type character varying(64),
                              description character varying(512),
                              duration smallint NOT NULL,
                              name character varying(128) NOT NULL,
                              updated_at timestamp(6) without time zone,
                              college_id bigint NOT NULL
);


ALTER TABLE public.program OWNER TO root;

--
-- Name: program_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.program ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
  SEQUENCE NAME public.program_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  );


--
-- Name: role; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.role (
                           id bigint NOT NULL,
                           name character varying(255)
);


ALTER TABLE public.role OWNER TO root;

--
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.role ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
  SEQUENCE NAME public.role_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  );


--
-- Name: tag; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.tag (
                          id bigint NOT NULL,
                          created_at timestamp(6) without time zone,
                          name character varying(255) NOT NULL,
                          updated_at timestamp(6) without time zone,
                          course_year_id bigint NOT NULL
);


ALTER TABLE public.tag OWNER TO root;

--
-- Name: tag_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.tag ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
  SEQUENCE NAME public.tag_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  );


--
-- Name: user_favorites; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.user_favorites (
                                     user_id bigint NOT NULL,
                                     course_id bigint NOT NULL
);


ALTER TABLE public.user_favorites OWNER TO root;

--
-- Name: user_role; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.user_role (
                                user_id bigint NOT NULL,
                                role_id bigint NOT NULL
);


ALTER TABLE public.user_role OWNER TO root;

--
-- Name: users; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.users (
                            id bigint NOT NULL,
                            created_at timestamp(6) without time zone,
                            email character varying(255) NOT NULL,
                            first_name character varying(255) NOT NULL,
                            last_name character varying(255) NOT NULL,
                            password character varying(255) NOT NULL,
                            updated_at timestamp(6) without time zone,
                            username character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO root;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
  SEQUENCE NAME public.users_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  );


--
-- Name: votes; Type: TABLE; Schema: public; Owner: root
--

CREATE TABLE public.votes (
                            id bigint NOT NULL,
                            vote_type character varying(255) NOT NULL,
                            material_id bigint NOT NULL,
                            user_id bigint NOT NULL,
                            CONSTRAINT votes_vote_type_check CHECK (((vote_type)::text = ANY ((ARRAY['UPVOTE'::character varying, 'DOWNVOTE'::character varying])::text[])))
);


ALTER TABLE public.votes OWNER TO root;

--
-- Name: votes_id_seq; Type: SEQUENCE; Schema: public; Owner: root
--

ALTER TABLE public.votes ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
  SEQUENCE NAME public.votes_id_seq
  START WITH 1
  INCREMENT BY 1
  NO MINVALUE
  NO MAXVALUE
  CACHE 1
  );


--
-- Name: acl_class id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_class ALTER COLUMN id SET DEFAULT nextval('public.acl_class_id_seq'::regclass);


--
-- Name: acl_entry id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_entry ALTER COLUMN id SET DEFAULT nextval('public.acl_entry_id_seq'::regclass);


--
-- Name: acl_object_identity id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_object_identity ALTER COLUMN id SET DEFAULT nextval('public.acl_object_identity_id_seq'::regclass);


--
-- Name: acl_sid id; Type: DEFAULT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_sid ALTER COLUMN id SET DEFAULT nextval('public.acl_sid_id_seq'::regclass);


--
-- Data for Name: acl_class; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.acl_class (id, class) VALUES (1, 'com.andrezzb.coursearchive.college.models.College');
INSERT INTO public.acl_class (id, class) VALUES (2, 'com.andrezzb.coursearchive.program.models.Program');
INSERT INTO public.acl_class (id, class) VALUES (3, 'com.andrezzb.coursearchive.course.models.Course');
INSERT INTO public.acl_class (id, class) VALUES (4, 'com.andrezzb.coursearchive.course.models.CourseYear');
INSERT INTO public.acl_class (id, class) VALUES (5, 'com.andrezzb.coursearchive.material.models.Tag');
INSERT INTO public.acl_class (id, class) VALUES (6, 'com.andrezzb.coursearchive.material.models.MaterialGroup');
INSERT INTO public.acl_class (id, class) VALUES (7, 'com.andrezzb.coursearchive.material.models.Material');
INSERT INTO public.acl_class (id, class) VALUES (8, 'com.andrezzb.coursearchive.material.models.Comment');


--
-- Data for Name: acl_entry; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (1, 1, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (2, 2, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (3, 3, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (4, 4, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (5, 5, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (6, 6, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (7, 7, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (8, 8, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (9, 9, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (10, 10, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (11, 11, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (13, 13, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (134, 44, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (15, 15, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (16, 16, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (17, 17, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (18, 18, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (19, 19, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (20, 20, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (21, 21, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (22, 22, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (23, 23, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (24, 24, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (25, 25, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (26, 26, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (27, 27, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (28, 28, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (29, 29, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (30, 30, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (31, 31, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (32, 32, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (33, 33, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (34, 34, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (35, 35, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (36, 36, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (37, 37, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (38, 38, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (39, 39, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (40, 40, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (41, 41, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (42, 42, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (43, 43, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (149, 45, 0, 2, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (150, 46, 0, 13, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (151, 47, 0, 14, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (152, 12, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (153, 12, 1, 14, 3, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (167, 14, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (168, 14, 1, 2, 7, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (169, 14, 2, 3, 7, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (170, 14, 3, 4, 7, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (171, 14, 4, 5, 7, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (172, 14, 5, 6, 7, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (173, 14, 6, 7, 7, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (174, 14, 7, 8, 7, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (175, 14, 8, 9, 7, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (176, 14, 9, 10, 7, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (177, 14, 10, 11, 7, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (178, 14, 11, 12, 7, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (179, 14, 12, 13, 7, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (180, 14, 13, 14, 15, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (181, 48, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (182, 49, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (183, 50, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (184, 51, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (185, 52, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (186, 53, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (187, 54, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (188, 55, 0, 2, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (189, 56, 0, 13, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (190, 57, 0, 14, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (191, 58, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (192, 59, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (193, 60, 0, 1, 31, true, false, false);
INSERT INTO public.acl_entry (id, acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES (194, 61, 0, 1, 31, true, false, false);


--
-- Data for Name: acl_object_identity; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (1, 1, '1', NULL, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (2, 1, '2', NULL, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (3, 1, '3', NULL, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (4, 2, '1', 3, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (5, 3, '1', 4, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (6, 1, '4', NULL, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (7, 1, '5', NULL, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (8, 1, '6', NULL, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (9, 2, '2', 2, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (10, 2, '3', 2, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (11, 2, '4', 2, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (13, 3, '2', 12, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (15, 3, '4', 12, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (16, 3, '5', 12, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (17, 4, '1', 14, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (18, 4, '2', 14, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (19, 4, '3', 14, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (20, 4, '4', 14, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (21, 5, '1', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (22, 5, '2', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (23, 5, '3', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (24, 5, '4', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (25, 5, '5', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (26, 5, '6', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (27, 6, '1', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (28, 7, '1', 27, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (29, 6, '2', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (30, 6, '3', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (31, 6, '4', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (32, 7, '2', 27, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (33, 7, '3', 27, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (34, 7, '4', 27, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (35, 7, '5', 29, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (36, 7, '6', 29, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (37, 7, '7', 27, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (38, 7, '8', 30, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (39, 7, '9', 30, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (40, 7, '10', 30, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (41, 7, '11', 30, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (42, 7, '12', 31, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (43, 5, '7', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (45, 8, '1', 38, 2, false);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (46, 8, '2', 38, 13, false);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (47, 8, '3', 38, 14, false);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (12, 2, '5', 2, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (14, 3, '3', 12, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (48, 1, '7', NULL, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (44, 7, '13', 30, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (49, 2, '6', 8, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (50, 3, '6', 12, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (51, 4, '6', 14, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (52, 7, '14', 30, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (53, 6, '5', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (54, 5, '8', 17, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (55, 8, '4', 52, 2, false);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (56, 8, '5', 52, 13, false);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (57, 8, '6', 52, 14, false);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (58, 5, '11', 18, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (59, 5, '12', 18, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (60, 5, '13', 18, 1, true);
INSERT INTO public.acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES (61, 5, '14', 18, 1, true);


--
-- Data for Name: acl_sid; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.acl_sid (id, principal, sid) VALUES (1, true, 'admin');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (2, true, 'ivan-balen');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (3, true, 'marko-radovic');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (4, true, 'luka-zulj');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (5, true, 'luka-gorjan');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (6, true, 'ivana-babic');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (7, true, 'mia-andreis');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (8, true, 'patrik-simac');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (9, true, 'luka-dedic');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (10, true, 'ivan-martic');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (11, true, 'luka-tomic');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (12, true, 'lara-dragovic');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (13, true, 'sara.krcelic-02');
INSERT INTO public.acl_sid (id, principal, sid) VALUES (14, true, 'ivan-horvat');


--
-- Data for Name: college; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.college (id, acronym, address, city, created_at, description, name, postcode, updated_at, website) VALUES (2, 'RiTEH', 'Vukovarska 58', 'Rijeka', '2024-06-21 16:18:23.724735', 'Fakultet objedinjuje djelatnost 11 zavoda. U sklopu zavoda djeluje 38 katedri i 50 laboratorija, a na Fakultetu djeluju i Računalni centar, Knjižnica, te Financijska služba, Služba nabave i komercijale, Služba općih i kadrovskih poslova, Služba studentske evidencije i Tehnička služba.', 'Tehnički fakultet Rijeka', 51000, '2024-06-21 16:18:23.724735', 'http://www.riteh.uniri.hr/');
INSERT INTO public.college (id, acronym, address, city, created_at, description, name, postcode, updated_at, website) VALUES (4, 'EFRI', 'Ivana Filipovića 4', 'Rijeka', '2024-06-22 11:57:01.05215', ' Naši programi razvijaju se i izvode u suradnji sa stručnjacima iz prakse, inozemnim znanstvenicima i studentima jer je za izvrsnost potrebna suradnja svih dionika. Stoga je izvrsnost ono što tražimo i od vas, jer vi ste pripadnici generacije koja će imati presudnu ulogu u transformaciji gospodarstva Hrvatske. Odluke koje ćete donositi i projekti koje ćete realizirati tijekom, a posebno nakon završenog studija, bit će utemeljene na teorijskim i praktičnim znanjima koja ćemo vam prenijeti.', 'Ekonomski fakultet Rijeka', 51000, '2024-06-22 11:57:01.05215', 'https://www.efri.uniri.hr/');
INSERT INTO public.college (id, acronym, address, city, created_at, description, name, postcode, updated_at, website) VALUES (5, 'PFRI', 'Studentska ulica 2', 'Rijeka', '2024-06-22 11:59:44.797282', 'Jedan od temeljnih ciljeva Pomorskog fakulteta je provedba kvalitetnog, učinkovitog i inovativnog visokog obrazovanja na svim razinama studija sukladno potrebama gospodarstva i društva. U nastavnom procesu koristimo visoko sofisticiranu opremu, simulatore, laboratorije, suvremeno opremljene učionice, radionice, plovila i poligone. Na raspolaganju studentima za savladavanje odabranih studijskih programa sa što većim uspjehom je više od 80 nastavnika i vanjskih suradnika i stručnjaka iz prakse', 'Pomorski fakultet Rijeka', 51000, '2024-06-22 11:59:44.797282', 'https://www.pfri.uniri.hr/web/hr/index.php');
INSERT INTO public.college (id, acronym, address, city, created_at, description, name, postcode, updated_at, website) VALUES (6, 'MEDRI', 'Braće Branchetta 20', 'Rijeka', '2024-06-22 12:02:23.274501', 'Vizija Medicinskog fakulteta je pozicioniranje u domaćem i međunarodnom okruženju kao vodeća, prepoznatljiva i konkurentna visokoškolska, znanstvena i stručna ustanova koja je tijesno povezana i prilagođena zahtjevima lokalne i šire zajednice.', 'Medicinski fakultet Rijeka', 51000, '2024-06-22 12:02:23.275007', 'https://medri.uniri.hr/');
INSERT INTO public.college (id, acronym, address, city, created_at, description, name, postcode, updated_at, website) VALUES (7, 'FER', 'Unska ulica 3', 'Zagreb', '2024-06-28 19:10:51.422824', 'FER je najveća i najutjecajnija znanstvena i obrazovna institucija u Hrvatskoj na području elektrotehnike, računarstva te informacijskih i komunikacijskih tehnologija.', 'Fakultet elektrotehnike i računarstva', 10000, '2024-06-28 19:11:45.322684', 'https://www.fer.unizg.hr/');


--
-- Data for Name: comment; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.comment (id, created_at, edited, text, updated_at, material_id, user_id) VALUES (4, '2024-06-29 13:34:16.676537', false, 'Odlicno, ima netko rjesenja?', '2024-06-29 13:34:16.676537', 14, 3);
INSERT INTO public.comment (id, created_at, edited, text, updated_at, material_id, user_id) VALUES (5, '2024-06-29 13:34:53.007653', true, 'B grupa je bila jako slična, probat ću je dodat kasnije', '2024-06-29 13:35:01.118219', 14, 14);
INSERT INTO public.comment (id, created_at, edited, text, updated_at, material_id, user_id) VALUES (6, '2024-06-29 13:35:24.533196', false, 'Super, hvala', '2024-06-29 13:35:24.533196', 14, 1);


--
-- Data for Name: course; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.course (id, acronym, created_at, credits, description, name, updated_at, year, program_id) VALUES (2, 'OPS', '2024-06-22 12:22:53.349644', 4, 'Definicija pojma i evolucija organizacije poslovnog sustava. Organizacijski oblici poslovnih sustava. Podizanje poslovnog sustava. Osnovni principi organizacije. Upravljivost sustava. Formalna i neformalna organizacija. Informacije u poslovnom sustavu. Bihevioralni pristup u teoriji organizacije. Tipovi organizacijskih struktura. Projektiranje organizacije poslovnog sustava. Organizacijske promjene. Vrednovanje poslova. Vlasništvo. Upravljanje. Rukovođenje. Timski rad. Poslovna politika.', 'Organizacija poslovnih sustava', '2024-06-22 12:22:53.349644', 3, 5);
INSERT INTO public.course (id, acronym, created_at, credits, description, name, updated_at, year, program_id) VALUES (4, 'ASP', '2024-06-22 12:24:10.995103', 7, 'Uvod: rješavanje problema, algoritam, pseudokod, tipovi podataka, vremenska zahtjevnost algoritama. Apstraktni tip podataka. Lista. Stog. Red. Rekurzija-iteracija. Algoritmi za sortiranje i pretraživanja podataka. Stabla. Grafovi. Hash tablice.', ' Algoritmi i strukture podataka', '2024-06-22 12:24:10.995103', 2, 5);
INSERT INTO public.course (id, acronym, created_at, credits, description, name, updated_at, year, program_id) VALUES (5, 'PROGING', '2024-06-22 12:27:11.751862', 7, 'Uvod u disciplinu programskog inženjerstva. Opći model životnog ciklusa programskog proizvoda uključujući analizu, specifikaciju, tehničko oblikovanje, implementaciju i testiranje zahtjeva. Metode i alati koji se koriste unutar svake faze životnog ciklusa programskog proizvoda. Upravljanje u disciplini programskog inženjerstva. Modeli koji se koriste za razvoj programskog proizvoda, vodopadni, spiralni, iterativni, inkrementalni i agilne metode. Inženjerstvo i tehničko oblikovanje programskog proizvoda.', 'Programsko inženjerstvo', '2024-06-22 12:27:11.752398', 3, 5);
INSERT INTO public.course (id, acronym, created_at, credits, description, name, updated_at, year, program_id) VALUES (6, 'URS', '2024-06-28 19:17:02.366469', 7, 'Razumijevanje arhitekture i načina korištenja mikrokontrolera. Razumijevanja principa i koncepata programiranja ugradbenih sustava. Stjecanje znanja i praktičnih iskustava u izvedbi sklopovske i programske komponente ugradbenih računalnih sustava.', 'Ugradbeni računalni sustavi', '2024-06-28 19:17:43.840572', 3, 5);
INSERT INTO public.course (id, acronym, created_at, credits, description, name, updated_at, year, program_id) VALUES (3, 'BP', '2024-06-22 12:23:28.53162', 6, 'Osnovni koncepti baza podataka i sustava za upravljanje bazama podataka. Modeli podataka. Relacijska algebra i relacijski model. Logičko oblikovanje baza podataka. Model entiteti-veze. Preslikavanje modela entiteti-veze u relacije. Strukturirani upitni jezik (SQL).', 'Baze podataka', '2024-07-03 14:35:26.676744', 3, 5);


--
-- Data for Name: course_year; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.course_year (id, academic_year, assistant, created_at, difficulty, enrollment_count, exercise_count, laboratory_count, lecture_count, passed_count, professor, updated_at, course_id) VALUES (1, '2023/2024', 'Alen Salkanović', '2024-06-22 13:22:10.097913', NULL, 50, NULL, 2, 2, NULL, 'Sandi Ljubić', '2024-06-22 13:22:10.097913', 3);
INSERT INTO public.course_year (id, academic_year, assistant, created_at, difficulty, enrollment_count, exercise_count, laboratory_count, lecture_count, passed_count, professor, updated_at, course_id) VALUES (3, '2021/2022', '', '2024-06-22 13:28:36.667239', NULL, 55, NULL, NULL, NULL, 42, '', '2024-06-28 19:19:09.91168', 3);
INSERT INTO public.course_year (id, academic_year, assistant, created_at, difficulty, enrollment_count, exercise_count, laboratory_count, lecture_count, passed_count, professor, updated_at, course_id) VALUES (2, '2022/2023', 'Alen Salkanović', '2024-06-22 13:23:47.47713', NULL, 45, NULL, 2, 2, 37, 'Sandi Ljubić', '2024-06-28 19:19:31.780662', 3);
INSERT INTO public.course_year (id, academic_year, assistant, created_at, difficulty, enrollment_count, exercise_count, laboratory_count, lecture_count, passed_count, professor, updated_at, course_id) VALUES (6, '2020/2021', 'Alen Salkanović', '2024-06-28 20:58:09.636193', 7, 54, NULL, 2, 2, 46, 'Sandi Ljubić', '2024-06-28 20:58:09.636193', 3);


--
-- Data for Name: file; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.file (id, created_at, mime_type, name, path, updated_at, material_id) VALUES (1, '2024-06-22 21:37:58.331941', 'image/png', 'Untitled.png', 'material/1/Untitled.png', '2024-06-22 21:37:58.331941', 1);
INSERT INTO public.file (id, created_at, mime_type, name, path, updated_at, material_id) VALUES (2, '2024-06-22 21:45:15.25143', 'image/png', 'Untitled.png', 'material/2/Untitled.png', '2024-06-22 21:45:15.25143', 2);
INSERT INTO public.file (id, created_at, mime_type, name, path, updated_at, material_id) VALUES (4, '2024-06-23 11:12:37.936115', 'image/png', 'Untitled.png', 'material/4/Untitled.png', '2024-06-23 11:12:37.936115', 4);
INSERT INTO public.file (id, created_at, mime_type, name, path, updated_at, material_id) VALUES (5, '2024-06-23 11:13:27.779385', 'image/png', 'Untitled.png', 'material/5/Untitled.png', '2024-06-23 11:13:27.779385', 5);
INSERT INTO public.file (id, created_at, mime_type, name, path, updated_at, material_id) VALUES (6, '2024-06-23 11:13:49.408548', 'image/png', 'Untitled.png', 'material/6/Untitled.png', '2024-06-23 11:13:49.408548', 6);
INSERT INTO public.file (id, created_at, mime_type, name, path, updated_at, material_id) VALUES (7, '2024-06-23 11:14:19.237119', 'image/png', 'Untitled.png', 'material/7/Untitled.png', '2024-06-23 11:14:19.237119', 7);
INSERT INTO public.file (id, created_at, mime_type, name, path, updated_at, material_id) VALUES (9, '2024-06-23 11:15:28.121878', 'image/png', 'Untitled.png', 'material/9/Untitled.png', '2024-06-23 11:15:28.121878', 9);
INSERT INTO public.file (id, created_at, mime_type, name, path, updated_at, material_id) VALUES (10, '2024-06-23 11:15:44.030286', 'image/png', 'Untitled.png', 'material/10/Untitled.png', '2024-06-23 11:15:44.030286', 10);
INSERT INTO public.file (id, created_at, mime_type, name, path, updated_at, material_id) VALUES (11, '2024-06-23 11:16:12.654273', 'image/png', 'Untitled.png', 'material/11/Untitled.png', '2024-06-23 11:16:12.654273', 11);
INSERT INTO public.file (id, created_at, mime_type, name, path, updated_at, material_id) VALUES (12, '2024-06-23 11:16:39.444471', 'image/png', 'Untitled.png', 'material/12/Untitled.png', '2024-06-23 11:16:39.444471', 12);
INSERT INTO public.file (id, created_at, mime_type, name, path, updated_at, material_id) VALUES (13, '2024-06-23 11:33:34.67728', 'image/png', 'Untitled.png', 'material/13/Untitled.png', '2024-06-23 11:33:34.67728', 13);
INSERT INTO public.file (id, created_at, mime_type, name, path, updated_at, material_id) VALUES (14, '2024-06-29 10:17:05.181324', 'image/png', 'Untitled.png', 'material/14/Untitled.png', '2024-06-29 10:17:05.181324', 14);


--
-- Data for Name: material; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.material (id, created_at, description, name, updated_at, vote_count, material_group_id) VALUES (4, '2024-06-23 11:12:37.918892', '', 'P4 - Preslikavanje u relacije', '2024-06-23 11:12:37.918892', 0, 1);
INSERT INTO public.material (id, created_at, description, name, updated_at, vote_count, material_group_id) VALUES (5, '2024-06-23 11:13:27.775179', '', 'BP - Uvod 23/24', '2024-06-23 11:13:27.775179', 0, 2);
INSERT INTO public.material (id, created_at, description, name, updated_at, vote_count, material_group_id) VALUES (6, '2024-06-23 11:13:49.404296', '', 'BP - Tehnologije i alati', '2024-06-23 11:13:49.404296', 0, 2);
INSERT INTO public.material (id, created_at, description, name, updated_at, vote_count, material_group_id) VALUES (12, '2024-06-23 11:16:39.440208', '', 'Zadaci za vježbu - ER Modeliranje', '2024-06-23 11:16:54.446634', 0, 4);
INSERT INTO public.material (id, created_at, description, name, updated_at, vote_count, material_group_id) VALUES (11, '2024-06-23 11:16:12.650053', '', 'Rješenja Završni Ispit - 1. Rok', '2024-06-23 11:31:59.707323', 0, 3);
INSERT INTO public.material (id, created_at, description, name, updated_at, vote_count, material_group_id) VALUES (1, '2024-06-22 21:37:58.322945', '', 'P1 - Uvod', '2024-06-23 11:41:31.479108', 12, 1);
INSERT INTO public.material (id, created_at, description, name, updated_at, vote_count, material_group_id) VALUES (13, '2024-06-23 11:33:34.671418', '', 'Pitanja Završni Ispit - 1. Rok', '2024-06-23 12:12:39.117558', 5, 3);
INSERT INTO public.material (id, created_at, description, name, updated_at, vote_count, material_group_id) VALUES (9, '2024-06-23 11:15:28.118185', '', 'Pitanja 1. Kolokvij - Grupa B', '2024-06-23 12:13:30.878742', -6, 3);
INSERT INTO public.material (id, created_at, description, name, updated_at, vote_count, material_group_id) VALUES (10, '2024-06-23 11:15:44.025923', '', 'Pitanja 2. Kolokvij - Grupa B', '2024-06-23 12:14:23.302854', 9, 3);
INSERT INTO public.material (id, created_at, description, name, updated_at, vote_count, material_group_id) VALUES (2, '2024-06-22 21:45:15.242848', '', 'P2 - Razvojni ciklus i relacijski model ', '2024-06-24 15:23:51.306', 8, 1);
INSERT INTO public.material (id, created_at, description, name, updated_at, vote_count, material_group_id) VALUES (7, '2024-06-23 11:14:19.232893', '', 'P3 - ER Modeliranje', '2024-06-24 15:23:53.295196', 5, 1);
INSERT INTO public.material (id, created_at, description, name, updated_at, vote_count, material_group_id) VALUES (14, '2024-06-29 10:17:05.162041', 'Sadrži pitanja sa 1. kolokvija, grupa A, bez rješenja', 'Pitanja 1. Kolokvij - Grupa A', '2024-06-29 11:23:02.595816', 14, 3);


--
-- Data for Name: material_group; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.material_group (id, created_at, description, display_order, name, updated_at, course_year_id) VALUES (3, '2024-06-22 21:38:55.57794', 'Materijali vezani za kolokvije ili ispite', 0, 'Ispiti', '2024-06-29 10:11:24.179922', 1);
INSERT INTO public.material_group (id, created_at, description, display_order, name, updated_at, course_year_id) VALUES (5, '2024-06-29 10:23:57.889714', 'Materijali koji mogu služiti kao pomoć kod pripreme za kolokvij ili ispit', 5, 'Materijali za vježbu', '2024-06-29 10:23:57.89024', 1);
INSERT INTO public.material_group (id, created_at, description, display_order, name, updated_at, course_year_id) VALUES (4, '2024-06-22 21:39:28.057549', 'Ostali materijali koji mogu biti korisni', 4, 'Ostalo', '2024-06-29 10:10:53.742208', 1);
INSERT INTO public.material_group (id, created_at, description, display_order, name, updated_at, course_year_id) VALUES (2, '2024-06-22 21:38:34.764846', 'Materijali sa vježbi', 2, 'Vježbe', '2024-06-29 10:11:08.856212', 1);
INSERT INTO public.material_group (id, created_at, description, display_order, name, updated_at, course_year_id) VALUES (1, '2024-06-22 21:36:52.925573', 'Materijali sa predavanja', 1, 'Predavanja', '2024-06-29 10:24:48.674831', 1);


--
-- Data for Name: material_tag; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.material_tag (material_id, tag_id) VALUES (1, 2);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (1, 5);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (2, 2);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (2, 5);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (4, 2);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (4, 5);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (5, 2);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (5, 6);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (6, 2);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (6, 6);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (7, 5);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (7, 2);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (9, 2);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (10, 3);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (12, 7);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (11, 4);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (13, 4);
INSERT INTO public.material_tag (material_id, tag_id) VALUES (14, 2);


--
-- Data for Name: program; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.program (id, created_at, degree_title, degree_title_abrev, degree_type, description, duration, name, updated_at, college_id) VALUES (3, '2024-06-22 12:17:48.060813', 'Sveučilišni prvostupnik (baccalaureus) inženjer brodogradnje', 'univ. bacc. ing. nav. arch.', 'prijediplomski', 'Sveučilišni prijediplomski studij brodogradnje pripremat će studente za sveučilišni diplomski studij brodogradnje, ali će im pružati i mogućnost zapošljavanja na odgovarajućim stručnim poslovima. Na prijediplomskom studiju brodogradnje polaznicima će se u razumnoj količini i na dovoljno visokoj razini davati znanje iz temeljnih tehničkih sadržaja.', 3, 'Brodogradnja', '2024-06-22 12:17:48.060813', 2);
INSERT INTO public.program (id, created_at, degree_title, degree_title_abrev, degree_type, description, duration, name, updated_at, college_id) VALUES (4, '2024-06-22 12:19:10.684104', 'Sveučilišni prvostupnik (baccalaureus) inženjer elektrotehnike', 'univ. bacc. ing. el.', 'prijediplomski', 'Sveučilišni prijediplomski studij elektrotehnike pripremat će studente za sveučilišni diplomski studij elektrotehnike, ali će im pružati i mogućnost zapošljavanja na odgovarajućim stručnim poslovima. Studij ima za cilj osposobljavanje studenata za primjenu temeljnih i specijalističkih znanja iz elektrotehnike, prepoznavanje, oblikovanje i rješavanje problema iz prakse, primjenu drugih stečenih znanja iz tehnike, matematike i računarstva.', 3, 'Elektrotehnika', '2024-06-22 12:19:10.684104', 2);
INSERT INTO public.program (id, created_at, degree_title, degree_title_abrev, degree_type, description, duration, name, updated_at, college_id) VALUES (2, '2024-06-22 12:16:08.90547', 'Sveučilišni prvostupnik (baccalaureus) inženjer strojarstva', 'univ. bacc. ing. mech.', 'prijediplomski', 'Sveučilišni prijediplomski studij strojarstva pripremat će studente za sveučilišni diplomski studij strojarstva, ali će im pružati i mogućnost zapošljavanja na odgovarajućim stručnim poslovima. Studij ima za cilj osposobljavanje studenata za primjenu temeljnih i specijalističkih znanja iz strojarstva, prepoznavanje, oblikovanje i rješavanje problema iz prakse, primjenu drugih stečenih znanja iz tehnike, matematike i računarstva.', 3, 'Strojarstvo', '2024-06-22 12:19:25.197307', 2);
INSERT INTO public.program (id, created_at, degree_title, degree_title_abrev, degree_type, description, duration, name, updated_at, college_id) VALUES (6, '2024-06-28 19:14:39.271195', 'Sveučilišni prvostupnik medicinsko laboratorijske dijagnostike', 'univ. bacc. med. lab. diag.', 'prijediplomski', 'Preddiplomski sveučilišni studij Medicinsko laboratorijska dijagnostika omogućuje studentu stjecanje sljedećih znanja', 3, 'Medicinsko laboratorijska dijagnostika', '2024-06-28 19:15:06.351251', 6);
INSERT INTO public.program (id, created_at, degree_title, degree_title_abrev, degree_type, description, duration, name, updated_at, college_id) VALUES (5, '2024-06-22 12:20:57.80862', 'Sveučilišni prvostupnik (baccalaureus) inženjer računarstva', 'univ. bacc. ing. comp.', 'prijediplomski', 'Sveučilišni prijediplomski studij računarstva pripremat će studente za sveučilišni diplomski studij računarstva, ali će im pružati i mogućnost zapošljavanja na odgovarajućim stručnim poslovima. Studij ima za cilj osposobljavanje studenata za primjenu temeljnih i specijalističkih znanja iz računarstva za karakterizaciju, projektiranje, izvedbu, eksploatiranje i održavanje informacijskih i računalnih sustava.', 3, 'Računarstvo', '2024-07-03 14:33:19.201858', 2);


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.role (id, name) VALUES (1, 'USER');
INSERT INTO public.role (id, name) VALUES (2, 'MANAGER');
INSERT INTO public.role (id, name) VALUES (3, 'ADMIN');


--
-- Data for Name: tag; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.tag (id, created_at, name, updated_at, course_year_id) VALUES (2, '2024-06-22 21:34:35.862391', '1. Kolokvij', '2024-06-22 21:34:35.862391', 1);
INSERT INTO public.tag (id, created_at, name, updated_at, course_year_id) VALUES (3, '2024-06-22 21:34:41.911517', '2. Kolokvij', '2024-06-22 21:34:41.911517', 1);
INSERT INTO public.tag (id, created_at, name, updated_at, course_year_id) VALUES (5, '2024-06-22 21:35:58.051526', 'Predavanje', '2024-06-22 21:35:58.051526', 1);
INSERT INTO public.tag (id, created_at, name, updated_at, course_year_id) VALUES (6, '2024-06-22 21:36:01.650824', 'Vježbe', '2024-06-22 21:36:01.650824', 1);
INSERT INTO public.tag (id, created_at, name, updated_at, course_year_id) VALUES (7, '2024-06-23 11:16:47.237644', 'Dodatni materijali', '2024-06-23 11:16:47.237644', 1);
INSERT INTO public.tag (id, created_at, name, updated_at, course_year_id) VALUES (8, '2024-06-29 12:00:39.200024', 'Zadaća', '2024-06-29 12:00:39.200024', 1);
INSERT INTO public.tag (id, created_at, name, updated_at, course_year_id) VALUES (4, '2024-06-22 21:34:51.517413', 'Završni Ispit', '2024-06-29 12:14:48.257168', 1);
INSERT INTO public.tag (id, created_at, name, updated_at, course_year_id) VALUES (11, '2024-07-03 15:55:15.557115', '1. Kolokvij', '2024-07-03 15:55:15.557115', 2);
INSERT INTO public.tag (id, created_at, name, updated_at, course_year_id) VALUES (12, '2024-07-03 15:55:22.380789', '2. Kolokvij', '2024-07-03 15:55:22.380789', 2);
INSERT INTO public.tag (id, created_at, name, updated_at, course_year_id) VALUES (13, '2024-07-03 15:55:26.528288', 'Zadaće', '2024-07-03 15:55:26.528288', 2);
INSERT INTO public.tag (id, created_at, name, updated_at, course_year_id) VALUES (14, '2024-07-03 15:55:30.811373', 'Završni ispit', '2024-07-03 15:55:37.270557', 2);


--
-- Data for Name: user_favorites; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.user_favorites (user_id, course_id) VALUES (1, 3);
INSERT INTO public.user_favorites (user_id, course_id) VALUES (1, 4);
INSERT INTO public.user_favorites (user_id, course_id) VALUES (14, 3);
INSERT INTO public.user_favorites (user_id, course_id) VALUES (2, 6);


--
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.user_role (user_id, role_id) VALUES (3, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (4, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (5, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (6, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (7, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (8, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (9, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (10, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (11, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (12, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (13, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (14, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO public.user_role (user_id, role_id) VALUES (2, 3);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (2, '2024-06-21 16:04:57.642023', 'admin@admin.com', 'Admin', 'Admin', '{bcrypt}$2a$10$V0.XIXmNr5fRxG5QmjZoIePcmTcM2HQ7diyDvL9cq51wKSbgcbBzS', '2024-06-26 19:57:02.34371', 'admin');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (6, '2024-06-23 11:22:34.273204', 'luka-gorjan@gmail.com', 'Luka', 'Gorjan', '{bcrypt}$2a$10$vO9fM7tpLklXL3avr6c/JedyZJ8PaSXl4PYTyN3yUDb0ev8fbUIn2', '2024-06-23 11:22:34.273204', 'luka-gorjan');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (9, '2024-06-23 11:22:59.818073', 'patrik.simac@outlook.com', 'Patrik', 'Šimac', '{bcrypt}$2a$10$v7lCthvJu5gyGbEoHZw03.ZUH2WTL5EEcnCzMKAA5f0Xcq1Ca0HLq', '2024-06-23 11:22:59.818073', 'patrik-simac');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (14, '2024-06-23 11:24:30.970017', 'skrcelic02@gmail.com', 'Sara', 'Krčelić', '{bcrypt}$2a$10$mYgq1bhgIkZtunHcEZl6yu.Le7/uO9LJsnau3RzpWBQcIwGQ.v3tq', '2024-06-23 11:24:30.970017', 'sara.krcelic-02');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (12, '2024-06-23 11:23:44.905069', 'ltomic@uniri.hr', 'Luka', 'Tomić', '{bcrypt}$2a$10$vc2qZtBCjXr5Z0dlBzcXWuPJna28R/gtiRBSN2NIc6iqMaRAbTIIO', '2024-06-23 11:23:44.905069', 'luka-tomic');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (5, '2024-06-23 11:22:25.789713', 'l_zulj2002@gmail.com', 'Luka', 'Žulj', '{bcrypt}$2a$10$5eNMzgF1bBKFqhtcHNyGiuYmPwRmeGzGBO1sPcVef8dajLKtdSp9K', '2024-06-23 11:22:25.789713', 'luka-zulj');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (13, '2024-06-23 11:24:05.777271', 'lara.dragovic@student.uniri.hr', 'Lara', 'Dragović', '{bcrypt}$2a$10$slqCjbw1ER7mcNmT4.3b2e/5QYc/hQxM0sq5b3soZUR9mSezREola', '2024-06-23 11:24:05.777271', 'lara-dragovic');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (3, '2024-06-23 11:21:25.406129', 'ivan-balen132@gmail.com', 'Ivan', 'Balen', '{bcrypt}$2a$10$OfqtofsTytL60zH7FyAT5eHLNU4BNGAytw7RTFakqpuIDmJuEvmYa', '2024-06-23 11:21:25.406129', 'ivan-balen');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (11, '2024-06-23 11:23:28.270149', 'imartic3@gmail.com', 'Ivan', 'Matrić', '{bcrypt}$2a$10$A0imnExgjTKk2UUJAQZ5UuShvBBZyJjMfqUmX.ozR3LkjnEkVP3sa', '2024-06-23 11:23:28.270149', 'ivan-martic');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (4, '2024-06-23 11:21:45.571858', 'mradovic@uniri.hr', 'Marko', 'Radović', '{bcrypt}$2a$10$.bHG9AxPOAOkt3WF/lCvuO7CwUaBCKuKYgapViOpu3TTXqmkly3Ai', '2024-06-23 11:21:45.571858', 'marko-radovic');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (7, '2024-06-23 11:22:42.640494', 'ivana-babic02@gmail.com', 'Ivana', 'Babić', '{bcrypt}$2a$10$/Oa5SUlMqCTTs2zoqrk/K.h73WJYbRLGl.qy81xQNLrjpk2j3UYqO', '2024-06-23 11:22:42.640494', 'ivana-babic');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (10, '2024-06-23 11:23:16.303735', 'luka-dedic@gmail.com', 'Luka', 'Dedić', '{bcrypt}$2a$10$B7HnU03emgJE1YiJeyzcpuvlU7Bu9Ld46KPSlz6G/5Oz6ijTGO5Zy', '2024-06-23 11:23:16.303735', 'luka-dedic');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (8, '2024-06-23 11:22:52.008641', 'mandreis02@gmail.com', 'Mia', 'Andreis', '{bcrypt}$2a$10$9uxz18lTj/kfCvVuaYms0uyBUtCVNFWlB4sRr2PO2UKkQdJ6lfgCC', '2024-06-23 11:22:52.008641', 'mia-andreis');
INSERT INTO public.users (id, created_at, email, first_name, last_name, password, updated_at, username) VALUES (1, '2024-06-21 15:03:12.613628', 'ihorvat3@student.uniri.hr', 'Ivan', 'Horvat', '{bcrypt}$2a$10$U4JPWz2iybccGLJZbKJHOOLJCtgkbEWVkAPn7KnkfsCNV21Ov4L2S', '2024-06-26 19:54:25.947489', 'ivan-horvat');


--
-- Data for Name: votes; Type: TABLE DATA; Schema: public; Owner: root
--

INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (3, 'UPVOTE', 1, 1);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (5, 'UPVOTE', 1, 3);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (6, 'UPVOTE', 1, 4);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (7, 'UPVOTE', 1, 5);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (8, 'UPVOTE', 1, 6);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (9, 'UPVOTE', 1, 7);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (10, 'UPVOTE', 1, 8);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (11, 'UPVOTE', 1, 9);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (12, 'UPVOTE', 1, 10);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (13, 'UPVOTE', 1, 11);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (14, 'UPVOTE', 1, 12);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (16, 'UPVOTE', 2, 1);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (18, 'UPVOTE', 2, 3);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (19, 'UPVOTE', 2, 4);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (20, 'UPVOTE', 2, 5);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (21, 'UPVOTE', 2, 6);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (22, 'UPVOTE', 2, 7);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (23, 'UPVOTE', 2, 8);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (24, 'UPVOTE', 2, 9);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (15, 'UPVOTE', 1, 2);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (40, 'UPVOTE', 13, 1);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (42, 'UPVOTE', 13, 3);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (43, 'UPVOTE', 13, 4);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (44, 'UPVOTE', 13, 5);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (47, 'UPVOTE', 13, 2);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (48, 'DOWNVOTE', 9, 1);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (50, 'DOWNVOTE', 9, 3);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (51, 'DOWNVOTE', 9, 4);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (52, 'DOWNVOTE', 9, 5);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (53, 'DOWNVOTE', 9, 6);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (54, 'DOWNVOTE', 9, 2);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (55, 'UPVOTE', 10, 1);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (57, 'UPVOTE', 10, 3);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (58, 'UPVOTE', 10, 4);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (59, 'UPVOTE', 10, 5);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (60, 'UPVOTE', 10, 6);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (61, 'UPVOTE', 10, 7);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (62, 'UPVOTE', 10, 8);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (63, 'UPVOTE', 10, 9);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (64, 'UPVOTE', 10, 10);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (70, 'UPVOTE', 7, 5);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (71, 'UPVOTE', 7, 6);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (72, 'UPVOTE', 7, 7);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (73, 'UPVOTE', 7, 8);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (74, 'UPVOTE', 7, 9);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (75, 'UPVOTE', 7, 10);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (79, 'DOWNVOTE', 7, 2);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (80, 'UPVOTE', 14, 1);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (82, 'UPVOTE', 14, 3);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (83, 'UPVOTE', 14, 4);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (84, 'UPVOTE', 14, 5);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (85, 'UPVOTE', 14, 6);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (86, 'UPVOTE', 14, 7);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (87, 'UPVOTE', 14, 8);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (88, 'UPVOTE', 14, 9);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (89, 'UPVOTE', 14, 10);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (90, 'UPVOTE', 14, 11);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (91, 'UPVOTE', 14, 12);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (92, 'UPVOTE', 14, 13);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (93, 'UPVOTE', 14, 14);
INSERT INTO public.votes (id, vote_type, material_id, user_id) VALUES (94, 'UPVOTE', 14, 2);


--
-- Name: acl_class_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.acl_class_id_seq', 8, true);


--
-- Name: acl_entry_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.acl_entry_id_seq', 194, true);


--
-- Name: acl_object_identity_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.acl_object_identity_id_seq', 61, true);


--
-- Name: acl_sid_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.acl_sid_id_seq', 14, true);


--
-- Name: college_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.college_id_seq', 7, true);


--
-- Name: comment_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.comment_id_seq', 6, true);


--
-- Name: course_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.course_id_seq', 6, true);


--
-- Name: course_year_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.course_year_id_seq', 6, true);


--
-- Name: file_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.file_id_seq', 14, true);


--
-- Name: material_group_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.material_group_id_seq', 5, true);


--
-- Name: material_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.material_id_seq', 14, true);


--
-- Name: program_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.program_id_seq', 6, true);


--
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.role_id_seq', 3, true);


--
-- Name: tag_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.tag_id_seq', 14, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.users_id_seq', 14, true);


--
-- Name: votes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: root
--

SELECT pg_catalog.setval('public.votes_id_seq', 94, true);


--
-- Name: acl_class acl_class_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_class
  ADD CONSTRAINT acl_class_pkey PRIMARY KEY (id);


--
-- Name: acl_entry acl_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_entry
  ADD CONSTRAINT acl_entry_pkey PRIMARY KEY (id);


--
-- Name: acl_object_identity acl_object_identity_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_object_identity
  ADD CONSTRAINT acl_object_identity_pkey PRIMARY KEY (id);


--
-- Name: acl_sid acl_sid_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_sid
  ADD CONSTRAINT acl_sid_pkey PRIMARY KEY (id);


--
-- Name: college college_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.college
  ADD CONSTRAINT college_pkey PRIMARY KEY (id);


--
-- Name: comment comment_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.comment
  ADD CONSTRAINT comment_pkey PRIMARY KEY (id);


--
-- Name: course course_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course
  ADD CONSTRAINT course_pkey PRIMARY KEY (id);


--
-- Name: course_year course_year_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course_year
  ADD CONSTRAINT course_year_pkey PRIMARY KEY (id);


--
-- Name: file file_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.file
  ADD CONSTRAINT file_pkey PRIMARY KEY (id);


--
-- Name: users index_username; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.users
  ADD CONSTRAINT index_username UNIQUE (username);


--
-- Name: material_group material_group_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.material_group
  ADD CONSTRAINT material_group_pkey PRIMARY KEY (id);


--
-- Name: material material_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.material
  ADD CONSTRAINT material_pkey PRIMARY KEY (id);


--
-- Name: material_tag material_tag_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.material_tag
  ADD CONSTRAINT material_tag_pkey PRIMARY KEY (material_id, tag_id);


--
-- Name: program program_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.program
  ADD CONSTRAINT program_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.role
  ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: tag tag_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.tag
  ADD CONSTRAINT tag_pkey PRIMARY KEY (id);


--
-- Name: course_year uk5ut09uh6gx97puptysv4t151f; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course_year
  ADD CONSTRAINT uk5ut09uh6gx97puptysv4t151f UNIQUE (course_id, academic_year);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.users
  ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: role uk8sewwnpamngi6b1dwaa88askk; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.role
  ADD CONSTRAINT uk8sewwnpamngi6b1dwaa88askk UNIQUE (name);


--
-- Name: material_group ukrhfdr8m7sp3fta6v09htpsnxe; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.material_group
  ADD CONSTRAINT ukrhfdr8m7sp3fta6v09htpsnxe UNIQUE (course_year_id, name);


--
-- Name: acl_sid unique_uk_1; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_sid
  ADD CONSTRAINT unique_uk_1 UNIQUE (sid, principal);


--
-- Name: acl_class unique_uk_2; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_class
  ADD CONSTRAINT unique_uk_2 UNIQUE (class);


--
-- Name: acl_object_identity unique_uk_3; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_object_identity
  ADD CONSTRAINT unique_uk_3 UNIQUE (object_id_class, object_id_identity);


--
-- Name: acl_entry unique_uk_4; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_entry
  ADD CONSTRAINT unique_uk_4 UNIQUE (acl_object_identity, ace_order);


--
-- Name: user_favorites user_favorites_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.user_favorites
  ADD CONSTRAINT user_favorites_pkey PRIMARY KEY (user_id, course_id);


--
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.user_role
  ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.users
  ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: votes votes_pkey; Type: CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.votes
  ADD CONSTRAINT votes_pkey PRIMARY KEY (id);


--
-- Name: course_year fk3whw086rmg6k7iwry32kprgtw; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course_year
  ADD CONSTRAINT fk3whw086rmg6k7iwry32kprgtw FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: user_favorites fk4sv7b9w9adr0fjnc4u10exlwm; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.user_favorites
  ADD CONSTRAINT fk4sv7b9w9adr0fjnc4u10exlwm FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: tag fk5qgdib0wunv8ca30c23svsc6r; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.tag
  ADD CONSTRAINT fk5qgdib0wunv8ca30c23svsc6r FOREIGN KEY (course_year_id) REFERENCES public.course_year(id);


--
-- Name: comment fk6cbtbgbkbtc78yodik4mif66f; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.comment
  ADD CONSTRAINT fk6cbtbgbkbtc78yodik4mif66f FOREIGN KEY (material_id) REFERENCES public.material(id);


--
-- Name: material_tag fk7towbm6q57xhajkktegsvojek; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.material_tag
  ADD CONSTRAINT fk7towbm6q57xhajkktegsvojek FOREIGN KEY (material_id) REFERENCES public.material(id);


--
-- Name: program fk8yxka9akreicomvu2uuqyu9wy; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.program
  ADD CONSTRAINT fk8yxka9akreicomvu2uuqyu9wy FOREIGN KEY (college_id) REFERENCES public.college(id) ON DELETE CASCADE;


--
-- Name: votes fk9gh7xmjuyvq21xjormtkb439k; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.votes
  ADD CONSTRAINT fk9gh7xmjuyvq21xjormtkb439k FOREIGN KEY (material_id) REFERENCES public.material(id);


--
-- Name: material_tag fka47yrhhm3trvqj6f2at1p9oix; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.material_tag
  ADD CONSTRAINT fka47yrhhm3trvqj6f2at1p9oix FOREIGN KEY (tag_id) REFERENCES public.tag(id);


--
-- Name: user_role fka68196081fvovjhkek5m97n3y; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.user_role
  ADD CONSTRAINT fka68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- Name: file fkdvvi9ssp9vrim4d83racaaane; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.file
  ADD CONSTRAINT fkdvvi9ssp9vrim4d83racaaane FOREIGN KEY (material_id) REFERENCES public.material(id);


--
-- Name: user_role fkj345gk1bovqvfame88rcx7yyx; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.user_role
  ADD CONSTRAINT fkj345gk1bovqvfame88rcx7yyx FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: course fkkrt3dma7ruv45lsafb5lu5x7r; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.course
  ADD CONSTRAINT fkkrt3dma7ruv45lsafb5lu5x7r FOREIGN KEY (program_id) REFERENCES public.program(id) ON DELETE CASCADE;


--
-- Name: votes fkli4uj3ic2vypf5pialchj925e; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.votes
  ADD CONSTRAINT fkli4uj3ic2vypf5pialchj925e FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: user_favorites fkmb2ggkulxg3lx4736uppxxf9k; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.user_favorites
  ADD CONSTRAINT fkmb2ggkulxg3lx4736uppxxf9k FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: material_group fko7iihbafjvj68tavne3tyh8us; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.material_group
  ADD CONSTRAINT fko7iihbafjvj68tavne3tyh8us FOREIGN KEY (course_year_id) REFERENCES public.course_year(id);


--
-- Name: comment fkqm52p1v3o13hy268he0wcngr5; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.comment
  ADD CONSTRAINT fkqm52p1v3o13hy268he0wcngr5 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: material fktasta74kmlgbsv9gp2c3d4wjh; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.material
  ADD CONSTRAINT fktasta74kmlgbsv9gp2c3d4wjh FOREIGN KEY (material_group_id) REFERENCES public.material_group(id);


--
-- Name: acl_object_identity foreign_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_object_identity
  ADD CONSTRAINT foreign_fk_1 FOREIGN KEY (parent_object) REFERENCES public.acl_object_identity(id);


--
-- Name: acl_object_identity foreign_fk_2; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_object_identity
  ADD CONSTRAINT foreign_fk_2 FOREIGN KEY (object_id_class) REFERENCES public.acl_class(id);


--
-- Name: acl_object_identity foreign_fk_3; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_object_identity
  ADD CONSTRAINT foreign_fk_3 FOREIGN KEY (owner_sid) REFERENCES public.acl_sid(id);


--
-- Name: acl_entry foreign_fk_4; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_entry
  ADD CONSTRAINT foreign_fk_4 FOREIGN KEY (acl_object_identity) REFERENCES public.acl_object_identity(id);


--
-- Name: acl_entry foreign_fk_5; Type: FK CONSTRAINT; Schema: public; Owner: root
--

ALTER TABLE ONLY public.acl_entry
  ADD CONSTRAINT foreign_fk_5 FOREIGN KEY (sid) REFERENCES public.acl_sid(id);


--
-- PostgreSQL database dump complete
--


CREATE TABLE measurements
(
  id bigint NOT NULL,
  name character varying(255),
  "timestamp" timestamp without time zone,
  unit integer,
  value double precision NOT NULL,
  CONSTRAINT measurements_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE hibernate_sequence START 1;
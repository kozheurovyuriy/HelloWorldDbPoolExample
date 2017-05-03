/*
     CREATE DATABASE hello;
*/
create table messages
(
  id integer not null
    constraint messages_id_pk
    primary key,
  value varchar(32)
)
;

INSERT INTO public.messages (id, value) VALUES (1, 'Hello World from DB');

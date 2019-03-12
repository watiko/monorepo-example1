create table todos
(
  id serial constraint todos_pk primary key,
  title varchar(256) not null,
  done boolean not null
);

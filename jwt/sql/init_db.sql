create table users
(
    id         serial primary key unique,
    first_name varchar(255) not null,
    last_name  varchar(255),
    email      varchar(255) not null,
    password   varchar(255) not null,
    role       varchar(255) not null
        constraint users_role_check check ((role)::text = ANY
        ((ARRAY [
        'USER':: character varying,
        'ADMIN':: character varying,
        'MANAGEMENT':: character varying
        ])::text[])
)
    );
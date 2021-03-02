create sequence hibernate_sequence start with 1 increment by 1

    create table users (
       id integer not null,
        password varchar(255),
        username varchar(255),
        primary key (id)
    )

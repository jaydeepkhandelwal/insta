create table hibernate_sequence
(
    next_val bigint null
)

create database insta;

create table users
(
    id              bigint       not null
        primary key,
    created_by      varchar(255) null,
    created_on      datetime     null,
    deleted         bit          null,
    last_updated_by varchar(255) null,
    last_updated_on datetime     null,
    email           varchar(255) null,
    name            varchar(255) null,
    password        varchar(255) null,
    phone_number    varchar(255) not null,
    ext_id          varchar(255) not null
)

create table media
(
    id              bigint       not null primary key,
    created_by      varchar(255) null,
    created_on      datetime     null,
    deleted         bit          null,
    ext_id          varchar(255) not null,
    last_updated_by varchar(255) null,
    last_updated_on datetime     null,
    description     varchar(255) null,
    path            varchar(255) null,
    title           varchar(255) null,
    user_id         bigint       null,
    FOREIGN KEY (user_id) REFERENCES users(id)

)


create table reviews
(
    id              bigint       not null
        primary key,
    created_by      varchar(255) null,
    created_on      datetime     null,
    deleted         bit          null,
    ext_id          varchar(255) not null,
    last_updated_by varchar(255) null,
    last_updated_on datetime     null,
    text            varchar(255) null,
    media_id        bigint       null,
    user_id         bigint       null,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (media_id) REFERENCES media(id)
)





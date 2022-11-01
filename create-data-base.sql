create table category
(
    id   mediumint auto_increment
        primary key,
    name varchar(256) not null,
    constraint category_name_uindex
        unique (name)
);

create table newsletter
(
    id   mediumint    not null
        primary key,
    name varchar(256) not null,
    constraint newsletter_name_uindex
        unique (name)
);

create table producer
(
    id   mediumint auto_increment
        primary key,
    name varchar(256) not null,
    constraint producer_name_uindex
        unique (name)
);

create table product
(
    id          mediumint auto_increment
        primary key,
    price       int           not null,
    amount      int           not null,
    name        varchar(256)  not null,
    description varchar(1024) not null,
    img_path    varchar(256)  not null,
    category_id mediumint     not null,
    producer_id mediumint     not null,
    constraint product_category_fk
        foreign key (category_id) references category (id)
            on update cascade on delete cascade,
    constraint product_producer_fk
        foreign key (producer_id) references producer (id)
            on update cascade on delete cascade
);

create table ordered_products_info
(
    id         mediumint auto_increment
        primary key,
    product_id mediumint not null,
    price      int       not null,
    amount     int       not null,
    constraint product_fk
        foreign key (product_id) references product (id)
            on delete cascade
);

create table role
(
    id   mediumint auto_increment
        primary key,
    name varchar(256) not null,
    constraint role_name_uindex
        unique (name)
);

create table user
(
    id               mediumint auto_increment
        primary key,
    login            varchar(256)   not null,
    first_name       varchar(256)   not null,
    second_name      varchar(256)   not null,
    password         varbinary(256) not null,
    email            varchar(256)   not null,
    profile_pic_path varchar(256)   null,
    constraint user_email_uindex
        unique (email),
    constraint user_login_uindex
        unique (login)
);

create table `order`
(
    id             mediumint auto_increment
        primary key,
    status         varchar(256) not null,
    status_detail  varchar(256) not null,
    detail_time    datetime     not null,
    user_id        mediumint    not null,
    payment_method varchar(256) not null,
    address        varchar(256) not null,
    constraint user_fk
        foreign key (user_id) references user (id)
);

create table order_card
(
    order_id        mediumint    not null
        primary key,
    cvv             int          not null,
    number          varchar(256) not null,
    expiration_date varchar(256) not null,
    constraint order_card_order_id_uindex
        unique (order_id),
    constraint order_id_card_fk
        foreign key (order_id) references `order` (id)
            on delete cascade
);

create table order_product
(
    order_id           mediumint not null,
    ordered_product_id mediumint null,
    constraint order_id_fk
        foreign key (order_id) references `order` (id)
            on delete cascade,
    constraint order_product_info_fk
        foreign key (ordered_product_id) references ordered_products_info (id)
            on delete cascade
);

create table user_newsletters
(
    user_id       mediumint not null,
    newsletter_id mediumint not null,
    constraint user_id
        unique (user_id, newsletter_id),
    constraint user_newsletters_ibfk_1
        foreign key (user_id) references user (id)
            on update cascade on delete cascade,
    constraint user_newsletters_ibfk_2
        foreign key (newsletter_id) references newsletter (id)
            on update cascade on delete cascade
);

create index newsletter_id
    on user_newsletters (newsletter_id);

create table user_roles
(
    user_id mediumint not null,
    role_id mediumint not null,
    constraint role_id_fk
        foreign key (role_id) references role (id)
            on delete cascade,
    constraint user_id_fk
        foreign key (user_id) references user (id)
            on delete cascade
);

INSERT INTO role values (1, 'USER');
INSERT INTO role values (2, 'ADMIN');
create database ssp_assignment
    with owner postgres;

grant connect, create, temporary on database ssp_assignment to ssp_assigment;

create table public.brand_group
(
    id      serial
        primary key,
    version integer default 0 not null,
    name    varchar           not null,
    lang    varchar(2)        not null
);

comment on column public.brand_group.version is 'Optimistic lock';

alter table public.brand_group
    owner to ssp_assigment;

create table public.brand
(
    id             serial
        constraint brand_pk
            primary key,
    version        integer default 0 not null,
    name           varchar(256)      not null,
    lang           varchar(2)        not null,
    brand_group_id integer
        constraint fk_brand_group_id
            references public.brand_group
);

alter table public.brand
    owner to ssp_assigment;

create table public.category
(
    id        serial
        constraint category_pk
            primary key,
    version   integer default 0 not null,
    name      varchar(256)      not null,
    lang      varchar(2)        not null,
    parent_id integer
        references public.category
);

comment on column public.category.parent_id is 'Using this or Parents (NOT BOTH!)... Depends on how to represent the prduct_type hierarchy.';

alter table public.category
    owner to ssp_assigment;

create table public.sku
(
    id                  integer not null
        constraint sku_category_fk
            references public.category,
    variant_id          integer not null,
    version             integer not null,
    lang                varchar(2),
    name                varchar(512),
    tshirt_size         varchar(6),
    numeric_size        double precision,
    numeric_size_region varchar(2),
    brand_id            integer not null
        constraint sku_brand_fk
            references public.brand,
    category_id         integer not null,
    constraint sku_pk
        primary key (id, variant_id)
);

comment on constraint sku_pk on public.sku is 'composite key';

alter table public.sku
    owner to ssp_assigment;


drop table if exists checkouts cascade;
drop table if exists bookings cascade;
drop table if exists instruments cascade;
drop table if exists users cascade;

create table if not exists users
(
    id       bigserial
        primary key,
    login    varchar(64) not null
        unique,
    password varchar(64) not null
);

create table if not exists instruments
(
    id               bigserial
        primary key,
    name             varchar(128)                           not null,
    type             text                                   not null
        constraint instruments_type_check
            check (type = ANY
                   (ARRAY ['PH_METER'::text, 'BALANCE'::text, 'SPECTROPHOTOMETER'::text, 'CONDUCTIVITY_METER'::text, 'THERMOMETER'::text])),
    inventory_number varchar(64)
        unique,
    location         varchar(128),
    status           text                                   not null
        constraint instruments_status_check
            check (status = ANY (ARRAY ['ACTIVE'::text, 'OUT_OF_SERVICE'::text])),
    owner_id         bigint
        references users,
    created_at       timestamp with time zone default now() not null,
    updated_at       timestamp with time zone default now() not null
);

create index if not exists idx_instruments_owner
    on instruments (owner_id);

create table if not exists bookings
(
    id            bigserial
        primary key,
    instrument_id bigint                                 not null
        references instruments,
    start_at      timestamp with time zone               not null,
    end_at        timestamp with time zone               not null,
    status        text                                   not null
        constraint bookings_status_check
            check (status = ANY (ARRAY ['ACTIVE'::text, 'CANCELLED'::text])),
    owner_id      bigint
        references users,
    owner_name    varchar(128),
    created_at    timestamp with time zone default now() not null,
    updated_at    timestamp with time zone,
    constraint bookings_check
        check (end_at >= start_at)
);

create index if not exists idx_bookings_instrument
    on bookings (instrument_id);

create index if not exists idx_bookings_owner
    on bookings (owner_id);

create table if not exists checkouts
(
    id               bigserial
        primary key,
    instrument_id    bigint                                 not null
        references instruments,
    username         varchar(64)                            not null,
    comment          varchar(128),
    taken_at         timestamp with time zone default now() not null,
    returned_at      timestamp with time zone,
    return_condition text
        constraint checkouts_return_condition_check
            check (return_condition = ANY (ARRAY ['OK'::text, 'DAMAGED'::text])),
    owner_id         bigint
        references users,
    created_at       timestamp with time zone default now() not null
);

create index if not exists idx_checkouts_instrument
    on checkouts (instrument_id);

create table club (
    id serial primary key,
    name text not null,
    address text not null
);

create table coach (
    id serial primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    year_of_birth int not null
);

create table club_when_open (
    club_id int,
    value_from time not null,
    value_to time not null,
    when_open_key varchar(9)
);

alter table club_when_open add foreign key(club_id) references club(id);


create table event_template (
    id serial primary key,
    title text not null,
    club_id int,
    start_time time,
    duration bigint,
    coach_id int,
    people_limit int,
    day varchar(9)
);

alter table event_template add foreign key(club_id) references club(id);
alter table event_template add foreign key(coach_id) references coach(id);

create table event (
    id serial primary key,
    title text not null,
    club_id int,
    start_time time,
    duration bigint,
    coach_id int,
    people_limit int,
    day varchar(9),
    event_date date

);

alter table event add foreign key(club_id) references club(id);
alter table event add foreign key(coach_id) references coach(id);


create table gym_bro (
                         id serial primary key,
                         first_name varchar(50),
                         last_name varchar(50),
                         account_created_at date,
                         gym_bro_type int,
                         deleted boolean
);

create table registration (
    id serial primary key,
    event_id int,
    gym_bro_id int
);

alter table registration add foreign key(event_id) references event(id);
alter table registration add foreign key(gym_bro_id) references gym_bro(id);
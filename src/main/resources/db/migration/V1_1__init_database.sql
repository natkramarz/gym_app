create table club (
    id int primary key auto_increment,
    name text not null,
    address text not null
);

create table coach (
    id int primary key auto_increment,
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
    id int primary key auto_increment,
    title text not null,
    club_id int,
    start_time timestamp without time zone,
    duration bigint,
    coach_id int,
    people_limit int,
    `day` varchar(9)
);

alter table event_template add foreign key(club_id) references club(id);
alter table event_template add foreign key(coach_id) references coach(id);

create table event (
    id int primary key auto_increment,
    title text not null,
    club_id int,
    start_time timestamp without time zone,
    duration bigint,
    coach_id int,
    people_limit int,
    `day` varchar(9),
    event_date date

);

alter table event add foreign key(club_id) references club(id);
alter table event add foreign key(coach_id) references coach(id);

create table registration (
    id int primary key auto_increment,
    event_id int not null,
    name varchar(50) not null,
    surname varchar(50) not null
);
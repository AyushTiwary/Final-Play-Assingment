# --- !Ups

create table if not exists userdatatable(
id serial NOT NULL,
firstname VARCHAR(20) NOT NULL,
middlename VARCHAR(20) ,
lastname VARCHAR(20) NOT NULL,
username VARCHAR(30) NOT NULL,
password VARCHAR NOT NULL,
mobileno bigint NOT NULL,
gender VARCHAR(10) NOT NULL,
age int NOT NULL,
isadmin boolean NOT NULL,
isenabled boolean NOT NULL,
PRIMARY KEY(id)
);

insert into userdatatable(firstname,lastname,username,password,mobileno,gender,age,isadmin
,isenabled) values('ayush','tiwari','ayush.tiwari@knoldus.in','$2a$10$n/CaKkXg8hm/DKqayF0.teZd2y8nDiiJGTAFFZVSZa.VsqGSuBcQy',
9560790485,'male',21,true,true);

create table if not exists hobbytable(
id serial NOT NULL,
hobby VARCHAR(20) NOT NULL
);

insert into hobbytable(hobby) values ('Chess'),('Table Tennis'),('Testing'),('Coding');

create table if not exists userhobbytable(
username VARCHAR(30) NOT NULL,
hobbyname VARCHAR(20) NOT NULL
);

create table if not exists assignmenttable(
id serial NOT NULL,
title VARCHAR(30) NOT NULL,
description VARCHAR NOT NULL,
PRIMARY KEY(id)
);

# --- !Downs

drop table userdatatable;
drop table hobbytable;
drop table userhobbytable;
drop table assignmenttable;
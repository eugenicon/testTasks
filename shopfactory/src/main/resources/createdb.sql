drop database if exists dbshops;
create database dbshops;
use dbshops;
create table shop (
  id int not null auto_increment,
  name varchar(100) not null,
  primary key (id)
);
create table category (
  id int not null auto_increment,
  name varchar(100) not null,
  id_shop int not null,
  primary key (id),
  foreign key (id_shop) references shop (id)
);
create table product_status (
  status varchar(100) not null,
  primary key (status)
);
create table product (
  id int not null auto_increment,
  title varchar(100) not null,
  id_category int not null,
  price float default 0,
  status varchar(100) not null,
  primary key (id),
  unique key (title),
  foreign key (status) references product_status (status),
  foreign key (id_category) references category (id)
);
insert into shop (id, name) values (1,'shop1'),(2,'shop2');
insert into category (id, name,id_shop) values (1,'cat1',1),(2,'cat2',1),
	(3,'cat3',2),(4,'cat4',2),(5,'cat5',2);
insert into product_status (status) values ('Available'),('Absent'),('Expected');


-- @author: Tabea, Maren

--------------------------------
-- Create the database tables --
--------------------------------


-- Datenbanktabelle für die HappyHour Daten
create table happyHour (
	id						identity not null,
	barID					varchar(50),
	description				varchar(255),
	start					time not null,
	end						time,
	monday					boolean not null,
	tuesday					boolean not null,
	wednesday				boolean not null,
	thursday				boolean not null,
	friday					boolean not null,
	saturday				boolean not null,
	sunday					boolean not null,
	constraint pk_happyHour primary key (id)
);

-- Datenbanktabelle für die gespeicherten Routen (Top Routen und Shared Routen)
create table route (
	hash				varchar(255),
	data				varchar(4096) not null,
	top					boolean not null,
	constraint pk_route primary key (hash)
);

-- Datenbanktabelle für die Bar-Meldungen durch einen User
create table barReport (
	id						identity not null,
	barID					varchar(50),
	description				varchar(255),
	constraint pk_barMeldung primary key (id)
);
-- Datenbanktabelle für die Administrator

create table user (
	name					varchar(50) not null,
	hashPw					varchar(50) not null,
	constraint pk_user primary key (name)
);


----------------------------
-- Foreign Keys and Rules --
----------------------------



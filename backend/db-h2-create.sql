-- @author: Tabea, Maren

--------------------------------
-- Create the database tables --
--------------------------------

-- testrun
create table testrun (
  id                        identity not null,
  name                      varchar(255),
  date                      timestamp,
  rounds                    bigint,
  constraint pk_testrun primary key (id)
);
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

----------------------------
-- Foreign Keys and Rules --
----------------------------

-- alter table notification add constraint fk_notification foreign key (barID) references barMeldung (barID) on delete cascade on update cascade;




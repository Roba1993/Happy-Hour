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
	hash				varchar(255) not null,
	data				varchar(4096) not null,
	top					boolean not null,
	constraint pk_route primary key (hash)
);

-- Datenbanktabelle für die Bar-Meldungen durch einen User

create table barReport (
	barID					varchar(50),
	description				varchar(255),
	reported				boolean not null,
	constraint pk_barMeldung primary key (barID)
);

----------------------------
-- Foreign Keys and Rules --
----------------------------

-- alter table notification add constraint fk_notification foreign key (barID) references barMeldung (barID) on delete cascade on update cascade;


------------------
-- Default Data --
------------------
INSERT INTO testrun (name, date, rounds) VALUES ('Admin', '2014-01-01 00:00:00', 12), ('User', '2013-02-02 00:00:00', 24);
INSERT INTO happyHour (barID, description, start, end, monday, tuesday, wednesday, thursday, friday, saturday, sunday) VALUES ('4b540ef4f964a5203db127e3', 'Alle Longdrinks und Cocktails für 4,50€', '22:00:00', '03:00:00', true, true, true, true, false, false, false);
INSERT INTO barReport (barID, description, reported) VALUES ('4b540ef4f964a5203db127e3', 'Bar existiert nicht mehr', true);
INSERT INTO barReport (barID, description, reported) VALUES ('lkajsd893kjh128jasljdlka', 'Bar ist out', false);
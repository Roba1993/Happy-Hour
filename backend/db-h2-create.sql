--------------------------------
-- Create the database tables --
--------------------------------

-- testrun
create table testrun (
  id                        identity not null,
  name                      varchar(255),
  date                      timestamp,
  rounds                    bigint,
  constraint pk_notification primary key (id))
;

----------------------------
-- Foreign Keys and Rules --
----------------------------

-- todo


------------------
-- Default Data --
------------------
INSERT INTO testrun (name, date, rounds) VALUES ('Admin', '2014-01-01 00:00:00', 12), ('User', '2013-02-02 00:00:00', 24);
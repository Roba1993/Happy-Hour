-- @author: Maren

--------------------------------
-- Insert data to the database tables --
--------------------------------

-- Fügt einen Testdatensatz in die HappyHour Datenbanktabelle ein
INSERT INTO happyHour (
barID, description, start, end, monday, tuesday, wednesday, thursday, friday, saturday, sunday
) 
VALUES 
('4b540ef4f964a5203db127e3', 'Alle Longdrinks und Cocktails für 4,50€', '22:00:00', '03:00:00', true, true, true, true, false, false, false); 

-- Fügt Test-Datensätze in die Route Datenbanktabelle ein
INSERT INTO route (hash, data, top)
VALUES 
('jakhsfdiuewhfwnvfk', 'JSON Objekt der TopRoute1', true),
('swhgkwvkjdsnvksjdh', 'JSON Objekt der TopRoute2', true),
('kjshgöeurhgkjeanvn', 'JSON Objekt einer Route', false);

-- Fügt Test-Datensätze in die barReport Datenbanktabelle ein
INSERT INTO barReport (id, barID, description)
VALUES
('100', 'liuhwqefnwqkjefk', 'Mit den Daten dieser Bar stimmt etwas nicht'),
('101', 'jskhfleuqrhvlqiu', 'Mit den Daten dieser Bar stimmt auch etwas nicht');

-- Fügt Test-Datensätze in die user Datenbanktabelle ein
INSERT INTO user (id, password)
VALUES
('12180', 'passw=rd');
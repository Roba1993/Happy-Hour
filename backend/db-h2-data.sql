-- @author: Maren

--------------------------------
-- Insert data to the database tables --
--------------------------------


INSERT INTO happyHour (
barID, description, start, end, monday, tuesday, wednesday, thursday, friday, saturday, sunday
) 
VALUES 
('4b540ef4f964a5203db127e3', 'Alle Longdrinks und Cocktails für 4,50€', '22:00:00', '03:00:00', true, true, true, true, false, false, false); 

INSERT INTO route (hash, data, top)
VALUES 
('jakhsfdiuewhfwnvfk', 'JSON Objekt der TopRoute1', true),
('swhgkwvkjdsnvksjdh', 'JSON Objekt der TopRoute2', true),
('kjshgöeurhgkjeanvn', 'JSON Objekt einer Route', false);

INSERT INTO barReport (id, barID, description)
VALUES
('100', 'liuhwqefnwqkjefk', 'Mit den Daten dieser Bar stimmt etwas nicht'),
('101', 'jskhfleuqrhvlqiu', 'Mit den Daten dieser Bar stimmt auch etwas nicht'),
('102', 'jskhfleuqrhvlqiu', 'Mit der Bar ist auch was falsch'),
('103', 'hhasd82njasu', 'Die Bar ist langweilig'),
('104', 'kkks881k2jn', 'Ich mag diese Bar nicht');

INSERT INTO user (id, password)
VALUES
('12180', 'passw=rd');
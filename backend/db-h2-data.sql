-- @author: Maren

--------------------------------
-- Dieses SQL-File fügt die Daten in die Datenbanktabellen ein --
--------------------------------

-- Die Daten sollten am Ende nocheinmals upgedated werden, in GitHub gibt es immer den aktuellen Datensatz --

-- Fügt die Datensätze in die Datenbanktabelle happHour
INSERT INTO happyHour (
barID, description, start, end, monday, tuesday, wednesday, thursday, friday, saturday, sunday
) 
VALUES 
('4b540ef4f964a5203db127e3' , 'Alle Longdrinks und Cocktails für 4,50€' , '22:00:00' , '03:00:00' , true , true , true , true , false , false , false),
('4b540ef4f964a5203db127e3' , 'Alle Longdrinks und Cocktails für 4,50€' , '00:00:00' , '03:00:00' , true , false , false , false , true , true , false),
('5346b3ad498e4ae9594fb6d5' , 'Alle Longdrinks und Cocktails für 4,90€; Alle Jumbo-Cocktails für 5,90€' , '22:30:00' , '00:00:00' , true , true , true , false , false , false , true),
('5346b3ad498e4ae9594fb6d6' , 'Alle Longdrinks und Cocktails für 4,90€; Alle Jumbo-Cocktails für 5,90€' , '22:30:00' , '01:00:00' , false , false , false , true , true , true , false),
('4d31d3c82e56236ac87f16b4' , 'Alle Cocktails für 4,90€' , '17:00:00' , '22:00:00' , true , true , true , true , false , false , true),
('52e96f1511d2c981acb41845' , 'Abendmahl im Menü: Burger mit Fritten und Cocktail für 7,50€: Burgerpreis plus 7,50€; Geöffnet bis Ende' , '17:00:00' , null , true , true , true , true , true , true , true),
('4c9521abf244b1f7f3e0281d' , 'Caipirinha und Mojito für 5€' , '18:00:00' , '01:00:00' , true , true , true , true , true , true , false),
('5001a910e4b0d8ae05eec43f' , 'Alle Cocktails 4,50€; Jumbos 6,50€; Margaritas für 3,95€: Margarita Pitcher für 15,95€' , '16:00:00' , '20:00:00' , true , true , true , true , true , true , true),
('5001a910e4b0d8ae05eec43f' , 'Margaritas für 3,95€: Margarita Pitcher für 15,95€' , '22:00:00' , '00:00:00' , true , true , true , true , false , false , true),
('5001a910e4b0d8ae05eec43f' , 'Margaritas für 3,95€: Margarita Pitcher für 15,95€' , '22:00:00' , '03:00:00' , false , false , false , false , true , true , false),
('4c9513a258d4b60c93e12e29' , 'Alle Cocktails für 5€ und Dinkelacker Privat 0,5l für 3€, 24h geöffnet' , '00:00:00' , '00:00:00' , false , false , false , false , false , false , true),
('4c9513a258d4b60c93e12e29' , 'Wodka Lemon, Bacardi Cola, Gin Tonic, 43 Milch, Jim Beam Cola, Batida Kirsch für 4,50€; 24h geöffnet' , '00:00:00' , '00:00:00' , true , true , true , false , false , false , false),
('4c87654b47cc224b99a4b09f' , 'Alle Cocktails to go für 4,99€; Geöffnet bis Ende' , '11:00:00' , null , true , true , true , true , true , false , false),
('4c87654b47cc224b99a4b09f' , 'Alle Cocktails to go für 4,99€, Geöffnet bis Ende' , '12:00:00' , null , false , false , false , false , false , true , true),
('4c87654b47cc224b99a4b09f' , 'Alle Cocktails für 5€' , '17:00:00' , null , false , false , false , true , false , false , false),
('4b7ee121f964a520590630e3' , 'Alle Cocktails für 4,90€' , '17:00:00' , '22:00:00' , false , true , true , true , true , true , true),
('4e0e11c27d8bb178a8a53262' , 'Jumbo Cocktail für 5€' , '17:00:00' , '18:00:00' , true , true , true , true , true , true , true),
('4e0e11c27d8bb178a8a53262' , 'Alle Cocktails zum halben Preis' , '18:00:00' , '20:00:00' , true , true , true , true , false , false , true),
('4e0e11c27d8bb178a8a53262' , 'Alle Cocktails zum halben Preis' , '17:00:00' , '20:00:00' , false , false , false , false , true , true , false),
('4e0e11c27d8bb178a8a53262' , 'Jumbo Cocktail zum Preis eines normalen' , '23:00:00' , '02:00:00' , true , true , true , true , false , false , false),
('4e0e11c27d8bb178a8a53262' , 'Jumbo Cocktail zum Preis eines normalen' , '23:00:00' , '04:00:00' , false , false , false , false , true , true , false),
('4e0e11c27d8bb178a8a53262' , 'Jumbo Cocktail zum Preis eines normalen' , '23:00:00' , '01:00:00' , false , false , false , false , false , false , true),
('4bd89025f645c9b6ab47a8e0' , 'Cocktail Happy Hour Double Time' , '21:00:00' , '23:00:00' , true , true , true , true , false , false , true),
('4b771002f964a52062792ee3' , 'Alle Cocktails für 4,50€' , '17:00:00' , '19:00:00' , true , true , true , true , true , false , false),
('4b771002f964a52062792ee3' , 'Alle Cocktails für 4,50€' , '16:00:00' , '19:00:00' , false , false , false , false , false , true , false),
('4b771002f964a52062792ee3' , 'Alle Cocktails für 4,50€' , '15:00:00' , '19:00:00' , false , false , false , false , false , false , true),
('4c2a4e48355cef3b27facc56' , 'Alle Cocktails für 5€' , '17:00:00' , '21:00:00' , true , true , true , true , true , true , true),
('4c2a4e48355cef3b27facc56' , 'Margaritas für 5€' , '17:00:00' , '01:00:00' , true , false , false , false , false , false , false),
('4c2a4e48355cef3b27facc56' , 'Alle Cocktails für 5€' , '17:00:00' , '01:00:00' , false , true , false , false , false , false , false),
('4c2a4e48355cef3b27facc56' , 'Für Studenten alle Cocktails für 5€' , '17:00:00' , '01:00:00' , false , false , true , false , false , false , false),
('4c2a4e48355cef3b27facc56' , 'Für Gruppen ab 3 Personen Cocktails für 5€' , '17:00:00' , '01:00:00' , false , false , false , true , false , false , false),
('4c2a4e48355cef3b27facc56' , '5 Cocktails zum Preis von 4' , '23:00:00' , '01:00:00' , false , false , false , false , true , true , false),
('4c2a4e48355cef3b27facc56' , 'Caipirinhas für 5€' , '17:00:00' , '01:00:00' , false , false , false , false , false , false , true),
('4fe4bdd5e4b0d43f1fceec16' , 'Alle Cocktails für 5€' , '17:30:00' , '20:00:00' , false , false , false , true , false , false , false),
('4e6f30be52b1706317b5b8f6' , 'Alle Cocktails für 5,50€' , '18:00:00' , '20:00:00' , true , true , true , true , true , true , true),
('4e6f30be52b1706317b5b8f6' , 'Ladies Night: Alle Cocktails für 5,50€' , '18:00:00' , '00:00:00' , false , false , true , false , false , false , false),
('4b7d9923f964a520f4c82fe3' , 'Alle Flaschenbiere 2€ (außer Echtes/Schwarzes)' , '23:00:00' , '02:00:00' , false , false , false , false , false , true , false),
('4b7d9923f964a520f4c82fe3' , 'Alle Getränke: Ein Großes bestellen - Ein Kleines bezahlen' , '23:00:00' , '02:00:00' , false , false , false , false , false , true , false),
('4b7d9923f964a520f4c82fe3' , 'Alle Cocktails und Longdrinks 1,50€ günstiger' , '23:00:00' , '02:00:00' , false , false , false , false , false , true , false);

-- Fügt Test-Datensätze in die route Datenbanktabelle ein
INSERT INTO route (hash, data, top)
VALUES 
('jakhsfdiuewhfwnvfk', 'JSON Objekt der TopRoute1', true),
('swhgkwvkjdsnvksjdh', 'JSON Objekt der TopRoute2', true),
('kjshgöeurhgkjeanvn', 'JSON Objekt einer Route', false);

-- Fügt Test-Datensätze in die barReport Datenbanktabelle ein
INSERT INTO barReport (id, barID, description)
VALUES
('100', 'liuhwqefnwqkjefk', 'Mit den Daten dieser Bar stimmt etwas nicht'),
('101', 'jskhfleuqrhvlqiu', 'Mit den Daten dieser Bar stimmt auch etwas nicht'),
('102', 'jskhfleuqrhvlqiu', 'Mit der Bar ist auch was falsch'),
('103', 'hhasd82njasu', 'Die Bar ist langweilig'),
('104', 'kkks881k2jn', 'Ich mag diese Bar nicht');

-- Fügt Test-Datensätze in die user Datenbanktabelle ein
INSERT INTO user (name, hashPw)
VALUES
('Admin', 'passw=rd');
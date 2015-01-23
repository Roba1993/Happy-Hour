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
('4b7d9923f964a520f4c82fe3' , 'Alle Cocktails und Longdrinks 1,50€ günstiger' , '23:00:00' , '02:00:00' , false , false , false , false , false , true , false),
('4b8070a1f964a520807230e3' , 'Alle Cocktails für 5€' , '17:00:00' , '20:00:00' , true , true , true , true , true , true , true),
('4b9d0c58f964a5201a8b36e3' , 'Auswahl an Happy Hour Cocktails für 4,90€' , '20:00:00' , '23:00:00' , true , true , true , true , true , true , true),
('4b9179c3f964a52067bf33e3' , 'Alle Cocktails zum halben Preis und alle Jumbos für nur 5,90€' , '18:00:00' , '20:00:00' , true , true , true , true , true , true , true),
('4b9179c3f964a52067bf33e3' , 'Alle Margaritas zum halben Preis und alle Jumbos für nur 5,90€' , '22:30:00' , '01:00:00' , true , true , true , true , false , false , true),
('4b9179c3f964a52067bf33e3' , 'Alle Margaritas zum halben Preis und alle Jumbos für nur 5,90€' , '22:30:00' , '02:00:00' , false , false , false , false , true , true , false),
('4b07024af964a5204bf522e3' , 'Zwei Bier zum Preis von einem.' , '18:00:00' , '20:00:00' , true , true , true , false , false , false , false),
('4b50a524f964a5201a2b27e3' , 'Alle Jumbo-Cocktails der Karte für 5,50€' , '17:00:00' , '20:00:00' , true , true , true , true , true , true , true),
('4b7d87fcf964a520d9c42fe3' , 'Alle Cocktails zum halben Preis' , '19:00:00' , '01:00:00' , true , true , true , true , true , true , true),
('4c153503a9c220a1f1c5589d' , 'Auswahl an Happy Hour Cocktails für 4,90€' , '20:00:00' , '03:00:00' , true , true , true , true , false , false , true),
('4c153503a9c220a1f1c5589d' , 'Auswahl an Happy Hour Cocktails für 4,90€' , '20:00:00' , '05:00:00' , false , false , false , false , true , true , false),
('4b59578af964a5209e8528e3' , 'Alle Cocktails für 4,90€' , '19:00:00' , '21:00:00' , true , true , true , true , true , true , true),
('4d02887137036dcb3eef03fb' , 'Alle Kaffeegetränke für 1,50€' , '15:00:00' , '06:00:00' , true , false , false , false , false , false , false),
('4d02887137036dcb3eef03fb' , 'Alle Cocktails für 4,50€' , '15:00:00' , '06:00:00' , false , false , true , false , false , false , false),
('4d02887137036dcb3eef03fb' , 'Krug Bier für 2,50€' , '15:00:00' , '06:00:00' , false , false , false , true , false , false , false),
('4d02887137036dcb3eef03fb' , 'Ein halber Krug Bier für 2,50€' , '15:00:00' , '06:00:00' , false , false , false , false , true , false , false),
('4ca097cf03133704c3f07bd5' , 'Alle Cocktails für 5€' , '11:00:00' , '22:00:00' , true , true , true , true , false , false , false),
('4babd691f964a520e1cd3ae3' , 'Jeden Tag Caipirinha für 5,50€ und Ouzo für 0,99€' , '10:00:00' , '03:00:00' , true , true , true , true , false , false , true),
('4babd691f964a520e1cd3ae3' , 'Jeden Tag Caipirinha für 5,50€ und Ouzo für 0,99€' , '10:00:00' , '04:00:00' , false , false , false , false , true , true , false),
('4b50a524f964a5201a2b27e3' , 'Jeden normalen Cocktail für 4,50€ und alle Jumbo-Cocktails für 5,90€' , '17:00:00' , '20:00:00' , true , true , true , true , true , true , true),
('4bb10b3ff964a52060753ce3' , 'Alle Cocktails für 4,90€' , '20:00:00' , '23:00:00' , true , true , true , true , true , true , true),
('4ba3d1a2f964a520e96238e3' , 'Alle alkoholischen Cocktails für 4€ und alle alkoholfreien Cocktails für 3€' , '19:00:00' , '20:00:00' , true , true , true , true , true , true , true),
('4b7592cdf964a5203b152ee3' , 'Ausgewählte Cocktails und Burger für jeweils 4€' , '23:00:00' , '02:00:00' , false , false , false , false , true , true , false),
('51c9d74d498e6c80b055d0be' , 'Alle Cocktails für 5,50€' , '20:00:00' , '00:00:00' , true , true , true , true , true , true , true);

-- Fügt Test-Datensätze in die route Datenbanktabelle ein
INSERT INTO route (hash, data, top)
VALUES 
('jakhsfdiuewhfwnvfk', '{"name":"Freitagsroute","options":{"radius":3,"location":{"longitude":9.1833333,"latitude":48.7666667},"startTime":"18:00","endTime":"23:00","stayTime":1,"weekday":5},"timeframes":[{"startTime":"18:00","endTime":"19:00","bar":{"id":"4ee01cbbe5fa0be3462093fc","name":"Schwarz Weiss Bar","rating":8.7,"costs":3,"description":"Cocktail Bar","imageUrl":"","location":{"longitude":9.1798935,"latitude":48.77067},"address":"Wilhelmstr. 8A","openingTimes":[{"barID":"4ee01cbbe5fa0be3462093fc","startTime":"19:00","endTime":"04:00","days":[5]},{"barID":"4ee01cbbe5fa0be3462093fc","startTime":"19:00","endTime":"04:00","days":[6]},{"barID":"4ee01cbbe5fa0be3462093fc","startTime":"20:00","endTime":"00:00","days":[7]},{"barID":"4ee01cbbe5fa0be3462093fc","startTime":"19:00","endTime":"00:00","days":[1]},{"barID":"4ee01cbbe5fa0be3462093fc","startTime":"21:00","endTime":"23:00","days":[2]},{"barID":"4ee01cbbe5fa0be3462093fc","startTime":"20:00","endTime":"02:00","days":[3]},{"barID":"4ee01cbbe5fa0be3462093fc","startTime":"19:00","endTime":"03:00","days":[4]}],"happyHours":[]}},{"startTime":"19:00","endTime":"20:00","bar":{"id":"4b7d9b43f964a52048c92fe3","name":"Fou Fou","rating":8.4,"costs":3,"description":"Bar","imageUrl":"","location":{"longitude":9.179137,"latitude":48.771656},"address":"Leonhardstr. 13","openingTimes":[{"barID":"4b7d9b43f964a52048c92fe3","startTime":"19:00","endTime":"03:00","days":[5]},{"barID":"4b7d9b43f964a52048c92fe3","startTime":"20:00","endTime":"02:00","days":[6]},{"barID":"4b7d9b43f964a52048c92fe3","startTime":"20:00","endTime":"00:00","days":[1]},{"barID":"4b7d9b43f964a52048c92fe3","startTime":"18:00","endTime":"00:00","days":[2]},{"barID":"4b7d9b43f964a52048c92fe3","startTime":"19:00","endTime":"00:00","days":[3,4]}],"happyHours":[]}},{"startTime":"20:00","endTime":"21:00","bar":{"id":"4bc77a468b7c9c744e8836cf","name":"Bix Jazzclub","rating":8.6,"costs":0,"description":"Jazz Club","imageUrl":"","location":{"longitude":9.179849,"latitude":48.773354},"address":"Leonhardplatz 28","openingTimes":[{"barID":"4bc77a468b7c9c744e8836cf","startTime":"16:00","endTime":"01:00","days":[5]},{"barID":"4bc77a468b7c9c744e8836cf","startTime":"17:00","endTime":"03:00","days":[6]},{"barID":"4bc77a468b7c9c744e8836cf","startTime":"19:00","endTime":"20:00","days":[1]},{"barID":"4bc77a468b7c9c744e8836cf","startTime":"19:00","endTime":"23:00","days":[2]},{"barID":"4bc77a468b7c9c744e8836cf","startTime":"17:00","endTime":"23:00","days":[3]},{"barID":"4bc77a468b7c9c744e8836cf","startTime":"16:00","endTime":"17:00","days":[4]}],"happyHours":[]}},{"startTime":"21:00","endTime":"22:00","bar":{"id":"4c49a57a93249c74a1cd0c4d","name":"Marshall Matt","rating":7.9,"costs":2,"description":"Bar","imageUrl":"","location":{"longitude":9.17882,"latitude":48.773926},"address":"Eberhardstr. 6","openingTimes":[{"barID":"4c49a57a93249c74a1cd0c4d","startTime":"15:00","endTime":"16:00","days":[5]},{"barID":"4c49a57a93249c74a1cd0c4d","startTime":"12:00","endTime":"03:00","days":[6]},{"barID":"4c49a57a93249c74a1cd0c4d","startTime":"15:00","endTime":"16:00","days":[7]},{"barID":"4c49a57a93249c74a1cd0c4d","startTime":"13:00","endTime":"14:00","days":[1]},{"barID":"4c49a57a93249c74a1cd0c4d","startTime":"14:00","endTime":"15:00","days":[2]},{"barID":"4c49a57a93249c74a1cd0c4d","startTime":"19:00","endTime":"01:00","days":[3]},{"barID":"4c49a57a93249c74a1cd0c4d","startTime":"19:00","endTime":"00:00","days":[4]}],"happyHours":[]}},{"startTime":"22:00","endTime":"23:00","bar":{"id":"4b584019f964a520994f28e3","name":"Mata Hari","rating":8.9,"costs":2,"description":"Bar","imageUrl":"","location":{"longitude":9.17767,"latitude":48.773727},"address":"Geißstr. 3","openingTimes":[{"barID":"4b584019f964a520994f28e3","startTime":"19:00","endTime":"03:00","days":[5]},{"barID":"4b584019f964a520994f28e3","startTime":"16:00","endTime":"17:00","days":[6]},{"barID":"4b584019f964a520994f28e3","startTime":"22:00","endTime":"23:00","days":[7]},{"barID":"4b584019f964a520994f28e3","startTime":"19:00","endTime":"23:00","days":[1]},{"barID":"4b584019f964a520994f28e3","startTime":"19:00","endTime":"00:00","days":[2,3]},{"barID":"4b584019f964a520994f28e3","startTime":"19:00","endTime":"02:00","days":[4]}],"happyHours":[]}}]}', true);

-- Fügt Test-Datensätze in die barReport Datenbanktabelle ein
INSERT INTO barReport (id, barID, description)
VALUES
('100', 'liuhwqefnwqkjefk', 'Mit den Daten dieser Bar stimmt etwas nicht'),
('101', 'jskhfleuqrhvlqiu', 'Mit den Daten dieser Bar stimmt auch etwas nicht'),
('102', 'jskhfleuqrhvlqiu', 'Mit der Bar ist auch was falsch'),
('103', 'hhasd82njasu', 'Die Bar ist langweilig'),
('104', 'kkks881k2jn', 'Ich mag diese Bar nicht');

-- Fügt Datensätze in die Admin-user Datenbanktabelle ein
INSERT INTO user (name, hashPw)
VALUES
('Admin', '4396cb8f54513b144667f5b6bab2fb95');
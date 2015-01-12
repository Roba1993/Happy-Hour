/*jshint unused: false, undef: false */
/**
 * Der Algorithmus erstellt aus den ihm gelieferten Informationen eine optimale Route durch Bars und Happy-Hours. 
 * Diese Daten werden in einem ersten Schritt zusammengetragen, validiert und aufbereitet um schlussendlich dem 
 * eigentlichen Algorithmus übergeben zu werden.
 *
 * @author Felix Rieder
 */
angular.module('happyHour.algorithm.RouteGenerator', [])
	.factory('RouteGeneratorService', [ function() {
		var service = {
			/**
			 * Erstellt eine optimale Route aus den vorhandenen Bars und Routenoptionen.
			 *
			 * @author Felix Rieder
			 * @param  {Bars[]} bars Die Bars die für die zu erstellende Route zur Auswahl stehen.
			 * @param  {Options} options Die vom Benutzer gegebenen Optionen die die Route beinflussen
			 * @return {Route} Ein vollständiges Routenobjekt. Bei einem Fehler ist der Rückgabewert `null`.
			 */
			createRoute: function(bars, options) {
				console.log(bars);
				console.log(options);
			}
		};

		return service;
	}]);
/*
## Objekt: Bars
Das Objekt für die Bars enthält die BarID und für jede Bar die Startzeit der Happyhour als Ganzzahl, die Dauer der Happyhour ebenfalls als Ganzzahl sowie die Bewertung der Bar als Gleitkommazahl.
```javascript
bars = {
    0: [BarID, Happyhour, Dauer, Bewertung],
    1: [BarID, Happyhour, Dauer, Bewertung],
    2: [BarID, Happyhour, Dauer, Bewertung]
    }
```
Beispiel:
```javascript
bars = {
    0: [Bar1, 1900, 120, 3.4],
    1: [Bar2, 2000, 60, 4.1],
    2: [Bar3, 2100, 120, 5.0]
    }
```
## Objekt: Distanzen
Dieses Objekt enthält die verschiedenen Distanzen zwischen den jeweiligen Bars genauso wie die Distanz des Benutzers oder des gewählten Ausgangspunkt zu den Bars.
Dabei ist das erste Element _immer_ userbezogen, d.h. es enthält die Entfernung des Benutzers zu den jeweiligen Bars.
Dabei folgt die Angabe der Distanzen der Reihenfolge in der die Bars angegeben werden. Aus Gründen der Einfachheit wird ebenso die Distanz zu der jeweiligen Bar angegeben was immer 0 ist. Die Einheit ist in Metern und immer eine Ganzzahl.
```javascript
var distances = {
    0: [DistanzUser, DistanzBar1, DistanzBar2, DistanzBar3],
    1: [DistanzUser, DistanzBar1, DistanzBar2, DistanzBar3],
    2: [DistanzUser, DistanzBar1, DistanzBar2, DistanzBar3],
    3: [DistanzUser, DistanzBar1, DistanzBar2, DistanzBar3]
}
```
Beispiel:
```javascript
var distances = {
    0:[0, 60, 80, 300]
    1:[20, 0, 100, 380],
    2:[10, 100, 0, 400],
    3:[500, 380, 400, 0]
}
```
*/
/*
 * Hilfsfunktion um aus einem String ein time-Objekt zu erstellen
 * @author Felix Rieder
 * @param {string} txt Text der in ein time-Objekt konvertiert werden soll; Format: 'hh:mm'; keine Validierung des Inputs!!
 * @return {time} t Gibt das Objekt von time zurueck
 */
function getTime(txt) {
    var t = time();
    t.hours(txt.slice(0, 1));
    t.minutes(txt.slice(3, 4));
    return t;
}

/*
 * Hilfsfunktion um einen string in einen integer - Wert umzuwandeln
 * @author Felix Rieder
 * @param {string} txt Stirng der die zu konvertierende Zeit enthaelt im Format 'hh:mm'
 * @return {integer} time Gibt die integer Repraesentation des strings zurueck
 */
function retIntTime(txt) {
    txt = txt.slice(0, 1) + txt.slice(3, 4);
    var time = parseInt(txt);
    return time;
}

/*
 * Hilfsfunktion um leere Elemente aus einem Array zu entfernen. Bedient sich der Eigenschafft das leere Elemente, undefined etc. 'false' returnen
 * @author Felix Rieder
 * @param {array} arr Das zu bereinigende Array
 *
 * @return {array} newarr Gibt das bereinigte Array aus
 */
function cleanArray(arr) {
    var newarr = [];
    for (var i = 0; i < arr.length; i++) {
        if (arr[i]) {
            newarr.push(arr[i]);
        }
    }
    return newarr;
}

/* 
 * @author Felix Rieder
 * Hilfsfunktion um einen dezimalen Wert in eine Bewertung/Rating zu konvertieren
 */
function convertDecimal(val) {
    val *= 100;
    Math.round(val);
    val = (50 - val) / 10;
    return val;
}

/*
 * Wandle die Distanzen in ein vergleichbares Rating um
 * distances: definiertes distances Array mit allen Distanzen von Google
 * base: index fuer array (int) um zu bestimmen welches Array der zweiten Dimension, sprich, welche Bar als Ausganspunkt fuer Distanzmessung zu nehmen
 */
function rateDistance(distances, base) {
    var n = distances[base].length;
    var total = 0;
    for (var i = 0; i < n; i++) {
        total += distances[0][i];
    }
    var rating = [];
    for (i = 0; i < n; i++) {
        rating[i] = convertDecimal(distances[i] / total);
    }
    return rating;
}

/* Waehle die naechste Bar aus unter Beruecksichtigung der Dauer der HappyHour sowie aller Ratings
 *
 * @author Felix Rieder
 * @param {array} distances Array mit allen Distanzen
 * @param {array} bars Array mit allen Bars; wird intern erzeugt
 * @param {string} startTime Startzeit zu der die naechste Bar gesucht wird; Format hh:mm
 * @param {int} count Gibt an die wievielte bar gesucht ist
 * @param {string} stayTime Gibt an wie lange der User in der Bar bleiben will; Format hh:mm
 * @returns {bar} bar Gibt die gesuchte Bar zurueck
 */
function chooseNext(distances, bars, startTime, count, stayTime) {
    startTime = retIntTime(startTime);
    stayTime = retIntTime(stayTime);
    var distanceRating = rateDistance(distances, count);

    var possibles = [];
    var ratioBoarder = 0.75;
    while (true) {
        for (var i = 0; i < bars.length; i++) {
            if (bars[i][1] - startTime === 0) {
                // wir haben eine Bar gefunden die passend zur Startzeit der Tour eine HappyHour hat!
                possibles[i] = bars[i];
            }
        }
        if (possibles.length === 0) {
            // keine Bar hat eine HappyHour zu der Zeit, ueberpruefe laufende HappyHours
            for (i = 0; i < bars.length; i++) {
                // checke ob die Startzeit innerhalb der HappyHour einer Bar liegt
                var happyHourEnd = bars[i][1] + bars[i][2];
                if (bars[i][1] < startTime && startTime < happyHourEnd) {
                    var ratio = (happyHourEnd - startTime) / stayTime;
                    if (ratio > ratioBoarder) {
                        possibles[i] = bars[i];
                    }
                }

            }
            possibles = cleanArray(possibles);
            if (0 < possibles.length) {
                if (possibles.length === 1) {
                    // Haben eine passende Bar gefunden
                    return possibles[0];
                } else {
                    // mehrere Bars kommen in Frage, waehle nach Rating aus
                    var merged = [];
                    // gleiche Rating in Bar array an
                    for (i = 0; i < distanceRating.length; i++) {
                        var rating = (distanceRating[i] + bars[i][1]) / 2 * 10;
                        merged[i][0] = bars[i][0]; // ID string of Bar
                        merged[i][1] = rating;
                    }
                    var temparr = [];
                    // Waehle nur die Bars aus, die auch in Frage kommen
                    for (i = 0; i < possibles.length; i++) {
                        for (var j = 0; j < bars.length; j++) {
                            if (possibles[i][0] === bars[j][0]) {
                                temparr.push(merged[j]);
                            }
                        }
                    }
                    // Sortiere das array mittels implementierter Array.sort() Funktion mit angepasster Vergleichsfunktion
                    // Sortiert wird die zweite Dimension
                    temparr.sort(function(a, b) {
                        if (a[1] === b[1]) {
                            return 0;
                        } else {
                            return (a[1] < b[1]) ? 1 : -1;
                        }
                    });
                    // Gib die beste Bar zurueck, ueberspringe aber die Erste weil die eigene Position immer die Beste ist und das Rating 5 bekommt
                    // Eventuell hier debuggen ob in distanceRating Array wirklich immer eine Bar mit Rating 5 auftaucht!
                    return temparr[1];
                }
            }
        } else {
            // Keine Bar hat eine laufende HappyHour die noch lang genug gilt. Verkleinere die Ratio fuer das Zielverhaeltnis Aufenthaltszeit/HappyHour
            // Es is unlogisch, dass es zu dieser Zeit keine auch nur minimal laufende HappyHour gibt.
            ratioBoarder -= 0.1;
            if (ratioBoarder < 0) {
                // ratioBoarder ist -0.5, ERROR
                return 0;
            }
        }
    }
}


/*
 * Waehle die erste Bar aus, basierend auf der Bewertung und der Distanz
 * @author Felix Rieder
 *
 * @param {array} distances Array das alle Distanzen enthaelt
 * @param {array} bars  internes, vorbereitetes Array mit allen bars
 * @param {string} startTime Startzeit zu der eine HappyHour gesucht wird
 * @return {Bar} bar Gibt die ausgewaehlte Bar zurueck
 */
function chooseFirst(distances, bars, startTime) {
    // Zuerst: Waehle Bars anhand Beginn der Happy Hour
    startTime = retIntTime(startTime);
    // Erstelle Rating fuer die Distanzen, waehle 0 als Base um Distanzen vom Standort des Users aus zu bewerten
    var distanceRating = rateDistance(distances, 0);
    var possibles = [];
    // Variable um Differenz anzugeben mit der es eine Bar in die Auswahl schafft
    var diff = 0;
    while (true) {
        for (var i = 0; i < bars.length; i++) {
            if (bars[i][1] - startTime === diff) {
                // wir haben eine Bar gefunden die passend zur Startzeit der Tour eine HappyHour hat!
                possibles[i] = bars[i];
            }
        }
        if (possibles.length === 0) {
            // erhoehe Timeframe um 15min
            diff += 15;
            if (diff >= 60) {
                // ERROR, Zeispanne unlogisch
                return 0;
            }
        } else {
            break;
        }
    }
    possibles = cleanArray(possibles);
    if (possibles.length > 1) {
        // mehrere Bars kommen als Startbar in Frage!
        // Vergleiche Bars mit gleichem Beginn der HappyHour anhand Bewertung und Distanz
        var merged = [];
        // gleiche Rating in Bar array an
        for (var k = 0; k < distanceRating.length; k++) {
            var rating = (distanceRating[k] + bars[k][1]) / 2 * 10;
            merged[k][0] = bars[k][0]; // ID string of Bar
            merged[k][1] = rating;
        }
        var temparr = [];
        // Waehle nur die Bars aus, die auch in Frage kommen
        for (var h = 0; h < possibles.length; h++) {
            for (var j = 0; j < bars.length; j++) {
                if (possibles[h][0] === bars[j][0]) {
                    temparr.push(merged[j]);
                }
            }
        }
        // Sortiere das array mittels implementierter Array.sort() Funktion mit angepasster Vergleichsfunktion
        // Sortiert wird die zweite Dimension
        temparr.sort(function(a, b) {
            if (a[1] === b[1]) {
                return 0;
            } else {
                return (a[1] < b[1]) ? 1 : -1;
            }
        });
        // Gib die beste Bar zurueck, ueberspringe aber die Erste weil die eigene Position immer die Beste ist und das Rating 5 bekommt
        // Eventuell hier debuggen ob in distanceRating Array wirklich immer eine Bar mit Rating 5 auftaucht!
        return temparr[1];
    } else {
        // wir haben exakt eine Bar die in Frage kommt.
        return possibles[0];
    }
}

/*
 * API Abruf an Google um Distanzen zu messen.
 * @author Felix Rieder
 *
 * @param {string} origins Startpunkte fuer Route, Adressen oder Laengen- und Breitengrade moeglich. Mehrere Startpunkte durch | getrennt.
 * @param {string} destinations Zielpunkte fuer Route, Adressen oder Laengen- und Breitengrade moeglich. Mehrere Zielpunkte durch | getrennt.
 */
function googleApiDist(origins, destinations) {
    var url = 'https://maps.googleapis.com/maps/api/distancematrix/json?origins=';
    if (origins.length > 1) {
        origins = origins.join('|');
    }
    if (destinations.length > 1) {
        destinations = destinations.join('|');
    }
    url = url + origins + '&destinations=' + destinations;
    // Setze sensor als True um anzugeben, dass der Standort des Nutzers mittels GPS Peilsender ermittelt wird
    // mode = walking damit google den fussweg berechnet
    url = url + '&sensor=True&mode=walking&language=de';
    var xmlreq = new XMLHttpRequest();
    xmlreq.open('GET', url, false);
    var response;
    if (xmlreq.readyState === 4) {
        response = xmlreq.responseText();
    }
    if (response !== null) {
        response = JSON.parse(response);
        // Parsing json
        var distances = [];
        for (var i = 0; i < response.rows.length; i++) {
            for (var j = 0; i < response.rows[i].elements.length; j++) {
                distances[i][j] = response.row[i].elements[j].distance.value;
            }
        }
        return distances;
    } else {
        console.log('Fehler bei der Abfrage der Google API. Anfrage nicht beendet oder erfolgreich.');
        return 0;
    }
}

/*
 * Hilfsfunktion um die Existenz eines Wertes in einem Array zu ueberpruefen.
 * Funktioniert ebenfalls in IE8
 * @author Felix Rieder
 */
var indexOf = function(needle) {
    if (typeof Array.prototype.indexOf === 'function') {
        indexOf = Array.prototype.indexOf;
    } else {
        indexOf = function(needle) {
            var i = -1,
                index = -1;

            for (i = 0; i < this.length; i++) {
                if (this[i] === needle) {
                    index = i;
                    break;
                }
            }

            return index;
        };
    }
    return indexOf.call(this, needle);
};

function createBarObject(rawbars, day) {
    var bars = [];
    for (var i = 0; i <= rawbars.length; i++) {
        bars[i][0] = rawbars[i].id;
        for (var t = 0; t <= rawbars[i].happyHours.length; t++) {
            var index = indexOf.call(rawbars[i].happyHours.days, day);
            if (index !== -1) {
                // Der Wert wurde im Array gefunden. Setze Startzeit.
                bars[i][1] = retIntTime(rawbars[i].happyHours.startTime);
                // Berechne Dauer
                var st = getTime(rawbars[i].happyHours.startTime);
                var tend = getTime(rawbars[i].happyHours.endTime);
                var newt = time();
                newt.add(tend.hours() - st.hours(), 'hours');
                if (tend.minutes() - st.minutes() < 0) {
                    newt.add(-1, 'hours');
                    newt.add(tend.minutes() - st.minutes() + 60, 'minutes');
                } else {
                    newt.add(tend.minutes() - st.minutes(), 'minutes');
                }
                // Setze Duration.
                bars[i][2] = newt.hours() * 60 + newt.minutes();
                bars[i][3] = rawbars[i].rating;
                break;
            }
        }
    }
    return bars;
}


/*
 * Hilfswerkzeug um die Bewertung der Bars anzupassen
 */
function adjustRating(bars, options) {
    for (var i = 0; i < bars.length; i++) {
        if (bars[i][2] - options.stayTime < 0) {
            bars[i][3] = 0;
        }
        // Wenn die HappyHour laenger als die Staytime ist erfolgt eine Abwertung, evtl. nicht sinnvoll!
        if (bars[i][2] - options.stayTime > 0) {
            (bars[i][3] - 1.5 < 0) ? bars[i][3] = 0: bars[i][3] - 1.5;
        }
    }
    return bars;
}


/*
 * Gibt ein Routen Objekt zurueck
 * bars: ist ein array bestehend aus mehreren 'bar'-json-objekten
 * options: ist ein array bestehend aus mehreren 'options' objekten
 */
function createRoute(jsonbars, options) {

    var bars = createBarObject(jsonbars, options.weekday);
    bars = adjustRating(bars, options);

    var barlocs = [];
    for (var i = 0; i <= bars.length; i++) {
        barlocs[i] = bars[i].location.latitude + ',' + bars[i].location.longitude;
    }
    var loc = options.location.latitude + ',' + options.location.longitude;
    // Startpunkt/Standort als location 0 in array:
    barlocs.splice(0, 0, loc);
    var distances = googleApiDist(barlocs, barlocs);
    if (distances === 0) {
        console.log('Failed.');
        return 0;
    }
    var startBar = chooseFirst(distances, bars, options.startTime);
    if (startBar === 0) {
        // ERROR
        return null;
    }
    //
    // ROUTE 
    //
    // ID ?! LINK ?! NAME ?!
    //
    var Route = {
        'id': 1,
        'link': 'e98723958987325md5',
        'name': 'Top Route',
        'options': options,
        'timeframes': []
    };

    for (var z = 0; z < jsonbars.length; z++) {
        if (jsonbars[z].id === startBar[0]) {
            var t = getTime(options.startTime);
            t.add(Math.floor(options.stayTime / 60), 'hours');
            t.add(options.stayTime % 60, 'minutes');
            Route.timeframes[0] = {
                'startTime': options.startTime,
                'endTime': t.toString().slice(0, 5),
                'bar': jsonbars[z]
            };
            break;
        }
    }
    var tbegin = getTime(options.startTime);
    var tend = getTime(options.endTime);
    var newt = time();
    newt.add(tend.hours() - tbegin.hours(), 'hours');
    if (tend.minutes() - tbegin.minutes() < 0) {
        newt.add(-1, 'hours');
        newt.add(tend.minutes() - tbegin.minutes() + 60, 'minutes');
    } else {
        newt.add(tend.minutes() - tbegin.minutes(), 'minutes');
    }
    // Berechne Duration.
    var duration = newt.hours() * 60 + newt.minutes();
    // berechne Anzahl der Bars fuer den Abend
    var barCount = Math.floor(duration / options.stayTime) - 1;
    // Uebrige Duration
    duration -= options.stayTime;

    // Waehle naechste Bars
    for (var y = 0; y < barCount; y++) {
        var nextBar = chooseNext(distances, bars, Route.timeframes[y].endTime, y + 1, duration);
        if (nextBar === 0) {
            // ERROR
            return null;
        }
        // Fuege Bar zum Routenobjekt hinzu
        for (var j = 0; j < jsonbars.length; j++) {
            if (jsonbars[j].id === nextBar[0]) {
                var et = getTime(Route.timeframes[j].endTime);
                et.add(Math.floor((duration < options.stayTime) ? duration / 60 : options.stayTime / 60), 'hours');
                et.add(options.stayTime % 60, 'minutes');
                Route.timeframes[j + 1] = {
                    'startTime': Route.timeframes[j].endTime,
                    'endTime': et.toString().slice(0, 5),
                    'bar': jsonbars[j]
                };
                break;
            }
        }
        duration -= options.stayTime;
    }
    return Route;
}
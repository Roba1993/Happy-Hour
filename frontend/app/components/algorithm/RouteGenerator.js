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
        var distances;
        
        /* Hilfsfunktion um mehrdimensionale Arrays zu erstellen.
         * @author http://stackoverflow.com/a/966938
         * @param {Integer} dim Anzahl der Dimensionen
         * @returns {Array} Gibt multidimensionales Array zurueck
         */
        function createArray(length) {
            var arr = new Array(length || 0),
                i = length;
        
            if (arguments.length > 1) {
                var args = Array.prototype.slice.call(arguments, 1);
                while(i--) { arr[length-1 - i] = createArray.apply(this, args); }
            }
        
            return arr;
        }	
        /*
         * Hilfsfunktion um einen string in einen integer - Wert umzuwandeln
         * @author Felix Rieder
         * @param {string} txt Stirng der die zu konvertierende Zeit enthaelt im Format 'hh:mm'
         * @return {integer} Gibt die integer Repraesentation des strings zurueck
         */
        function retIntTime(txt) {
            if(txt.length < 5) {
                txt += '0';
            }
            txt = txt.substring(0, 2) + txt.substring(3, 5);
            var time = parseInt(txt);
            return time;
        }
        
        /*
         * Hilfsfunktion um aus einem String ein time-Objekt zu erstellen
         * @author Felix Rieder
         * @param {string} txt Text der in ein time-Objekt konvertiert werden soll; Format: 'hh:mm'; keine Validierung des Inputs!!
         * @return {time} Gibt das Objekt von time zurueck
         */
        function getTime(txt) {
            if(txt.length < 5) {
                txt += '0';
            }
            var t = time(txt);
            return t;
        }
        
        /*
         * Hilfsfunktion um leere Elemente aus einem Array zu entfernen. Bedient sich der Eigenschafft das leere Elemente, undefined etc. 'false' returnen
         * @author Felix Rieder
         * @param {array} arr Das zu bereinigende Array
         *
         * @return {array} Gibt das bereinigte Array aus
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
         * @author Felix Rieder
         * @param {decimal} Dezimalwert der zu einem Rating (Bereich 0-5, schlecht - gut) konvertiert werden soll. Keine Input-Validierung!!
         * @return {decimal} Gibt das erstellte Rating zurueck. 
         */
        function convertDecimal(val) {
            val *= 100;
            val = Math.floor(val);
            val = (50 - val) / 10;
            return val;
        }
        
        /*
         * Wandle die Distanzen in ein vergleichbares Rating um. Je kuerzer die Distanz desto besser faellt das Rating aus.
         * @author Felix Rieder
         * @param {array} distances definiertes distances Array mit allen Distanzen von Google. Wird bei der Abfrage der Google Api erzeugt.
         * @param {int} base Index fuer das array um zu bestimmen welches Array der zweiten Dimension, sprich, welche Bar als Ausgangspunkt fuer Distanzmessung zu nehmen ist
         * @return {array} rating Gibt ein rating fuer die einzelnen Distanzen zurueck.
         */
        function rateDistance(distances, base) {
            var n = distances[base].length;
            var total = 0;
            for (var i = 0; i < n; i++) {
                total += distances[0][i];
            }
            var rating = [];
            for (i = 0; i < n; i++) {
                rating[i] = convertDecimal(distances[base][i] / total);
            }
            return rating;
        }
        
        /* Waehle die naechste Bar aus unter Beruecksichtigung der Dauer der HappyHour sowie aller Ratings
         *
         * @author Felix Rieder
         * @param {array} distances Array mit allen Distanzen
         * @param {bars[]} bars Array mit allen Bars; wird intern erzeugt
         * @param {string} startTime Startzeit zu der die naechste Bar gesucht wird; Format hh:mm
         * @param {int} count Gibt an die wievielte bar gesucht ist
         * @param {string} stayTime Gibt an wie lange der User in der Bar bleiben will; Format hh:mm
         * @return {bar} Gibt die gesuchte Bar zurueck
         */
        function chooseNext(distances, bars, startTime, count, stayTime) {
            startTime = retIntTime(startTime);
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
         * @param {bars[]} bars  internes, vorbereitetes Array mit allen bars
         * @param {string} startTime Startzeit zu der eine HappyHour gesucht wird
         * @return {Bar} Gibt die ausgewaehlte Bar zurueck
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
         * Hilfsfunktion um die bloede asznchronitaet von googles API JS auszugleichen.
         * @author http://www.devign.me/asynchronous-waiting-for-javascript-procedures
         * @param {function} function Die Methode die true zurueck geben muss damit weiter gemacht wird
         * @param {function} function2 Der Code der weiter ausgefuehrt werden soll
         * @param {integer} delay Die Verzoegerung zwischen den checks. Optional.
         * @param {integer} timeout Der timeout wie oft die Bedingung ueberprueft werden soll. Optional.
         */
        function waitUntil(check, onComplete, delay, timeout) {
            // if the check returns true, execute onComplete immediately
            if (check()) {
                onComplete();
                return;
            }
        
            if (!delay) { delay = 100; }
        
            var timeoutPointer;
            var intervalPointer = setInterval(function() {
                if (!check()) { return; } // if check didn't return true, means we need another check in the next interval
        
                // if the check returned true, means we're done here. clear the interval and the timeout and execute onComplete
                clearInterval(intervalPointer);
                if (timeoutPointer) { clearTimeout(timeoutPointer); }
                onComplete();
            }, delay);
            // if after timeout milliseconds function doesn't return true, abort
            if (timeout) { 
                timeoutPointer = setTimeout(
                    function() {
                        clearInterval(intervalPointer);
                    }, timeout);
            }
        }
        /*
         * Callback Funktion zur asynchronen Abarbeitung des Google Maps Api Request
         * @author Felix Rieder
         * @param {object} response Das Objekt der antwort von Google. JSON!
         * @param {string} status Der Status Coder der Abfrage.
         */
        function callback(response, status) {
            if (status !== google.maps.DistanceMatrixStatus.OK) {
                console.log('Fehler beim Zugriff auf die Google API: ' + status);
                return false;
            } else {
                for (var i = 0; i < response.rows.length; i++) {
                    for (var j = 0; j < response.rows[i].elements.length; j++) {
                        distances[i][j] = response.rows[i].elements[j].distance.value;
                    }
                }
            }
        }
        
        /*
         * API Abruf an Google um Distanzen zu messen.
         * @author Felix Rieder
         *
         * @param {array} bars Das array mit den benoetigten Informationen ueber die Bars
         * @param {object} options Das options Objekt
         */
        function googleApiDist(jsonbars, options) {
            distances = createArray(jsonbars.length+1, jsonbars.length+1);
        
            var service = new google.maps.DistanceMatrixService();
            
            var barlocs = createArray(jsonbars.length, 2);
            for (var o = 0; i < jsonbars.length; i++) {
                barlocs[o][0] = jsonbars[i].location.latitude;
                barlocs[o][1] = jsonbars[i].location.longitude;
            }
            var loc = [];
            loc[0] = options.location.latitude;
            loc[1] = options.location.longitude;
            // Startpunkt/Standort als location 0 in array:
            barlocs.splice(0, 0, loc); 
            
            for(var i = 0; i < barlocs.length; i++) {
                barlocs[i] = new google.maps.LatLng(barlocs[i][0], barlocs[i][1]);
            }
            var origs = barlocs;
            var dests = barlocs;
            service.getDistanceMatrix({
                origins: origs,
                destinations: dests,
                travelMode: google.maps.TravelMode.WALKING,
                unitSystem: google.maps.UnitSystem.METRIC,
            }, callback);
        }
        
        /*
         * Erstellt anhand Bars[] und dem Tag aus Options ein abgespecktes Array fuer die Bar-Informationen zur internen Verwendung.
         * @author Felix Rieder
         * @param {Bar[]} rawbars Array mit den zur Auswahl stehenden Bars als Bar-Objekte
         * @param {integer} day Nummer des Wochentages um die benoetigte Happy Hour aus dem Bar Objekt auszulesen.
         * @return {array} Gibt ein zweidimensionales Array zurueck. Definition siehe Comment.
         */
        function createBarObject(rawbars, day) {
            /*
             * Hilfsfunktion um die Existenz eines Wertes in einem Array zu ueberpruefen.
             * Funktioniert ebenfalls in IE8, aus: http://stackoverflow.com/questions/1181575/javascript-determine-whether-an-array-contains-a-value
             * @author http://stackoverflow.com/questions/1181575/javascript-determine-whether-an-array-contains-a-value
             * @param {element} needle Das zu suchende Element des Arrays. Kann alles moegliche sein. 
             * @return {integer} Gibt die Position des zu suchenden Elements zurueck. Enthaelt das Array den gesuchten Wert nicht ist der Ruckgabewert -1.
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
            
            var bars = createArray(rawbars.length, 4);
            for (var i = 0; i < rawbars.length; i++) {
                bars[i][0] = rawbars[i].id;
                for (var t = 0; t < rawbars[i].happyHours.length; t++) {
                    var index = indexOf.call(rawbars[i].happyHours[t].days, day);
                    if (index !== -1) {
                        // Der Wert wurde im Array gefunden. Setze Startzeit.
                        bars[i][1] = retIntTime(rawbars[i].happyHours[t].startTime);
                        // Berechne Dauer
                        var st = getTime(rawbars[i].happyHours[t].startTime);
                        var tend = getTime(rawbars[i].happyHours[t].endTime);
                        var newt = time();
                        newt.add(tend.hours() - st.hours(), 'hours');
                        if (tend.minutes() - st.minutes() < 0) {
                            newt.add(-1, 'hours');
                            // BUG IN timejs!!! (infinite object.add()!! )
                            //newt.add(tend.minutes() - st.minutes() + 60, 'minutes');
                            //besser:
                            var mins = tend.minutes() - st.minutes();
                            mins += 60;
                            newt.add(mins, 'minutes');
                        } else {
                            var mins2 = tend.minutes() - st.minutes();
                            if(mins2 !== 0) {
                                newt.add(mins2, 'minutes');
                            }
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
         * Hilfswerkzeug um die Bewertung der Bars anzupassen, hinsichtlich der Dauer der Happy-Hour im Vergleich der gewuenschten Verweildauer
         * @author Felix Rieder
         * @param {array} bars Das array mit den benoetigten Informationen ueber die Bars
         * @param {object} options Das options Objekt
         * @return {array} Gibt das modifizierte bars Array zurueck mit den angepassten ratings.
         */
        function adjustRating(bars, options) {
            for (var i = 0; i < bars.length; i++) {
                if (bars[i][2] - options.stayTime < 0) {
                    bars[i][3] = 0;
                }
                // Wenn die HappyHour laenger als die Staytime ist erfolgt eine Abwertung, evtl. nicht sinnvoll!
                if (bars[i][2] - options.stayTime > 0) {
                    (bars[i][3] - 1.5 < 0) ? bars[i][3] = 0 : bars[i][3] -= 1.5;
                }
            }
            return bars;
        }
        
		var service = {
            /*
             * Gibt ein Routen Objekt zurueck mit der moeglichst logischen Route durch verschiedene Bars.
             * Benoetigt UNBEDINGT google API JS Code:
             * <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBkJJTY5c87WY8kYx7WXKgdH6q2OHfygE0"></script>
             * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
             * @author Felix Rieder
             * @param {Bars[]} jsonbars ist ein array bestehend aus mehreren 'bar'-json-objekten
             * @param {Options} options Definiertes Objekt mit allen benoetigten Rahmeninformationen.
             * @return {Route} Gibt ein Routenobjekt zurueck. Bei einem Fehler ist der Rueckgabewert 'null'.
             */
            createRoute: function(jsonbars, options) {
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
                
                googleApiDist(jsonbars, options);
                
                function finalize() {
                    var bars = createBarObject(jsonbars, options.weekday);
                    bars = adjustRating(bars, options);
                
                    if (distances === 0) {
                        alert('Failed.');
                        return 0;
                    }
            
                    var startBar = chooseFirst(distances, bars, options.startTime);
                    if (startBar === 0) {
                        // ERROR
                        return null;
                    }
                
                    for (var z = 0; z < jsonbars.length; z++) {
                        if (jsonbars[z].id === startBar[0]) {
                            var t = getTime(options.startTime);
                            var ho = Math.floor(options.stayTime / 60);
                            t.add(ho, 'hours');
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
                            // keine Bar konnte ausgewaehlt werden, es ist jedoch noch Verweildauer uebrig!
                            var at = getTime(Route.timeframes[y].endTime);
                            at.add(duration, 'minutes');
                            Route.timeframes[y].endTime = at.toString().slice(0, 5);
                        } else {
                            // Fuege Bar zum Routenobjekt hinzu
                            for (var j = 0; j < jsonbars.length; j++) {
                                if (jsonbars[j].id === nextBar[0]) {
                                    var et = getTime(Route.timeframes[y].endTime);
                                    et.add(Math.floor((duration < options.stayTime) ? duration / 60 : options.stayTime / 60), 'hours');
                                    et.add(options.stayTime % 60, 'minutes');
                                    Route.timeframes[y + 1] = {
                                        'startTime': Route.timeframes[y].endTime,
                                        'endTime': et.toString().slice(0, 5),
                                        'bar': jsonbars[j]
                                    };
                                    break;
                                }
                            }
                        }
                        duration -= options.stayTime;
                    }
                }
                
                waitUntil(function() {
                    if(!distances[0][0]) {
                        return true;
                    }
                }, finalize);
                
                if(Route == null) {
                    return null;
                } else if(Route.timeframes.length > 0) {
                    return Route;
                } else {
                    return null;
                }
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

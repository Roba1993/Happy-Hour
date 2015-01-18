/**
 * Der Algorithmus erstellt aus den ihm gelieferten Informationen eine optimale Route durch Bars und Happy-Hours. 
 * Diese Daten werden in einem ersten Schritt zusammengetragen, validiert und aufbereitet um schlussendlich dem 
 * eigentlichen Algorithmus übergeben zu werden.
 *
 * @author Felix Rieder
 */
angular.module('happyHour.algorithm.RouteGenerator', [])
	.factory('RouteGeneratorService', [function() {
		var service = {
			/*
			 * Erstellt eine optimale Route aus den vorhandenen Bars und Routenoptionen.
			 *
			 * @author Markus Thömmes
			 * @param {Bars[]} bars Die Bars die für die zu erstellende Route zur Auswahl stehen.
			 * @param {Options} options Die vom Benutzer gegebenen Optionen die die Route beinflussen.
			 * @return {Route} Ein vollständiges Routenobjekt. Bei einem Fehler ist der Rückgabewert `null`.
			 */
			createRoute: function(bars, options) {
				var startTime = time(options.startTime);
				var endTime = time(options.endTime);

				// Timeframe-Array erstellen
				var timeframes = [];
				while(isBeforeOverNight(startTime, endTime)) {
					timeframes.push({
						startTime: startTime.toString(),
						endTime: startTime.add(options.stayTime, 'hours').toString(),
						bar: null
					});
				}

				console.log(timeframes);

				_.forEach(timeframes, function(timeframe, index) {
					// fromLocation entweder auf absoluten Startpunkt, bzw vorherige Bar setzen
					var fromLocation = options.location;
					if(index > 0) {
						fromLocation = timeframes[index-1].bar.location;
					}

					// Bars nach dem Score sortieren
					var sortedBars = _.sortBy(bars, function(bar) {
						return score(timeframe, fromLocation, bar);
					});
					
					// Den Timeframe mit der Bar befüllen, die den höchsten Score hat
					timeframe.bar = _.last(sortedBars);

					// Die bereits benutzte Bar aus der weiteren Auswahl entfernen
					bars = _.without(bars, timeframe.bar);
				});

				// Route zusammenstellen
				var route = {
					name: '',
					options: options,
					timeframes: timeframes
				};

				return route;
			}
		};

		/**
		 * Bestimmt ob eine Zeit vor der anderen ist, über die Nacht gesehen
		 * @param  {Time} time1 Erste Zeit
		 * @param  {Time} time2 Zweite Zeit
		 * @return {Boolean} `true` wenn time1 < time2
		 */
		function isBeforeOverNight(time1, time2) {
			if(time1.isAfter(time('12:00:00'))) {
				return !time1.isBefore(time2);
			}
			else {
				return time1.isBefore(time2);
			}
		}

		/**
		 * Bewertet eine Bar
		 * @param  {Timeframe} timeframe Zeitraum, indem die Bar platziert werden soll
		 * @param  {Location} fromLocation Die Lokation von der aus die nächste Bar angesteuert wird
		 * @param  {Bar} bar Die Bar die bewertet werden soll
		 * @return {Double} Der Score nach dem sortiert werden kann
		 */
		function score(timeframe, fromLocation, bar) {
			var distance = distanceBetween(fromLocation, bar.location);
			var rating = bar.rating;

			// TODO nicht vorhandenes Rating beachten
			// TODO timeframe/happyHour mit einbeziehen

			return rating/distance;
		}

		/**
		 * Berechnet die Distanz zwischen zwei Koordinaten (siehe: http://www.movable-type.co.uk/scripts/latlong.html)
		 * @param  {Location} from Das erste Koordinatenpaar
		 * @param  {Location} to Das zweite Koordinatenpaar
		 * @return {Double} Die Distanz zwischen beiden Koordinaten in Metern
		 */
		function distanceBetween(from, to) {
			var lat1 = toRadians(from.latitude);
			var lat2 = toRadians(to.latitude);
			var lonDiff = toRadians(to.longitude-from.longitude); 
			var earthRadius = 6371;

			return Math.acos(Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2) * Math.cos(lonDiff) ) * earthRadius;
		}

		/**
		 * Wandelt Gradzahlen in Radianten um
		 * @param  {Double} number Die umzuwandelnde Zahl
		 * @return {Double} Die eingegebene Zahl in Radianten
		 */
		function toRadians(number) {
			return number * (Math.PI/180);
		}

		return service;
	}]);
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
						return score(timeframe, fromLocation, bar, options.weekday);
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
		 * Berechnet die Sekunden zwischen 2 Zeiten
		 * @param  {Time} time1 Die erste Zeit
		 * @param  {Time} time2 Die zweite Zeit
		 * @return {Integer} Die Sekunden zwischen beiden Zeiten
		 */
		function secondsBetween(time1, time2) {
			return toSeconds(time2) - toSeconds(time1);
		}

		/**
		 * Berechnet das Sekundenäquivalent einer Uhrzeit
		 * @param  {Time} time Das `time`-Objekt das umgerechnet werden soll
		 * @return {Integer} Das Sekundenäquivalent
		 */
		function toSeconds(time) {
			return time.hours()*60*60 + time.minutes()*60 + time.seconds();
		}

		/**
		 * Bestimmt ob eine Zeit vor der anderen ist, über die Nacht gesehen
		 * @param  {Time} time1 Erste Zeit
		 * @param  {Time} time2 Zweite Zeit
		 * @return {Boolean} `true` wenn time1 < time2
		 */
		function isBeforeOverNight(time1, time2) {
			var referenceTime = time('12:00:00');
			if(time1.isAfter(referenceTime) && time2.isBefore(referenceTime)) {
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
		 * @param {Integer} weekday Der Wochentag der Route
		 * @return {Double} Der Score nach dem sortiert werden kann
		 */
		function score(timeframe, fromLocation, bar, weekday) {
			var distance = distanceBetween(fromLocation, bar.location);
			var rating = bar.rating;
			var happyHourOverlap = 0;

			// TODO nicht vorhandenes Rating beachten
			// TODO timeframe/happyHour mit einbeziehen
			
			// passende HappyHour finden
			var happyHour = null;
			_.forEach(bar.happyHours, function(hour) {
				if(_.contains(hour.days, weekday)) {
					happyHour = hour;
				}
			});

			// wenn eine HappyHour gefunden wurde berechnen wie genau sie auf den Timeframe passt
			if(happyHour !== null) {
				var timeframeStart = time(timeframe.startTime);
				var timeframeEnd = time(timeframe.endTime);
				var happyHourStart = time(happyHour.startTime);
				var happyHourEnd = time(happyHour.endTime);

				// ist eine Überlappung vorhanden?
				if(
					(happyHourStart.isAfter(timeframeStart) && happyHourStart.isBefore(timeframeEnd)) ||
					(happyHourEnd.isAfter(timeframeStart) && happyHourEnd.isBefore(timeframeEnd)) ||
					happyHourStart.isSame(timeframeStart) || happyHourEnd.isSame(timeframeEnd)
				) {
					// Überlappungsanteil berechnen
					var overlapStart = timeframeStart;
					if(happyHourStart.isAfter(timeframeStart)) {
						overlapStart = happyHourStart;
					}
					var overlapEnd = timeframeEnd;
					if(happyHourEnd.isBefore(timeframeEnd)) {
						overlapEnd = happyHourEnd;
					}

					var timeframeDuration = secondsBetween(timeframeStart, timeframeEnd);
					var overlapDuration = secondsBetween(overlapStart, overlapEnd);
					happyHourOverlap = overlapDuration/timeframeDuration;
				}
			}

			return (rating/distance) * (1+happyHourOverlap);
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
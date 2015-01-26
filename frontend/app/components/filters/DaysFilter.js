/**
 * Erstellt aus einem Array in dem die Wochentage als Nummern hinterlegt sind die Wochentage in lesbarer Form.
 *
 * @author Dorothee Nies
 * @param {Integer[]|Integer} input Die Wochentage in Nummernrepräsentation
 * @param {Boolean} isLong Tage in Langform angeben oder nicht
 * @return {String} Eine Aufzählung der Wochentage, getrennt durch einen Schrägstrich.
 */
angular.module('happyHour.filters.DaysFilter', [])
	.filter('formatDays', function() {
		return function(input, isLong) {
			if (!(input instanceof Array)){
				input = [input];
			}
			// Durch das einfügen von "null" an erster Position wird der Index des Arrays
			// so verschoben, dass er den Wochentagen entspricht
			var dayNames = [null, 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'];
			if (isLong){
				dayNames = [null, 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag'];
			}
			
			var translatedDays = [];
			
			// Wenn jeder Tag im Array vorhanden ist
			if (input.length === 7){
				return 'Jeden Tag';
			}
			
			// Jedes Input-Element wird durch den entsprechenden Wochentag ersetzt
			_.forEach(input, function(dayNumber) {
				if (dayNumber >= 1 && dayNumber <= 7){
					translatedDays.push(dayNames[dayNumber]);
				}
			});
			
			// Zusammenfügen des Arrays mit Schrägstrich als Trennzeichen
			return translatedDays.join('/');
		};
	});
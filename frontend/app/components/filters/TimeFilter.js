/**
 * Erstellt aus einem Objekt, das `startTime` und `endTime` enthält ein lesbares Zeitraumformat
 *
 * @author Dorothee Nies
 * @param {Object} Ein Objekt mit den Variablen `startTime` und `endTime`
 * @return {String} Ein Zeitraum des Formats "hh:mm - hh:mm" beziehungsweise "ab hh:mm" für einen Zeitraum ohne definiertes Ende.
 */
angular.module('happyHour.filters.TimeFilter', [])
	.filter('formatTime', function() {
		return function(input) {
			if(input.startTime === null) {
				return '';
			}
			
			// Open-End wenn endTime = null
			if (input.endTime === null) {	
				return 'ab ' + input.startTime;
			}
			else {
				return input.startTime + ' - ' + input.endTime;
			}
		};
	});
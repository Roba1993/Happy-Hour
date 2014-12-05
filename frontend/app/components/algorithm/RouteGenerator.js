/**
 * Der Algorithmus erstellt aus den ihm gelieferten Informationen eine optimale Route durch Bars und Happy-Hours. 
 * Diese Daten werden in einem ersten Schritt zusammengetragen, validiert und aufbereitet um schlussendlich dem 
 * eigentlichen Algorithmus übergeben zu werden.
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
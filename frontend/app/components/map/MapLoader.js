/**
 * Dient dem asynchronen Laden der Google Maps JavaScript API Version 3
 * Der Service liefert beim Laden über die Depedency Injection ein Promise, das beim
 * Auflösen ein google.maps Objekt zurückgibt
 *
 * @author Markus Thömmes
 */
angular.module('happyHour.map.MapLoader', [])
	.factory('MapLoaderService', ['$q', '$window', '$document', function($q, $window, $document) {
		// Promise erstellen
		var deferred = $q.defer();
		// Wenn die API schon geladen ist, das Promise sofort resolven und die Funktion abbrechen (return des Promises)
		if (angular.isDefined($window.google) && angular.isDefined($window.google.maps)) {
			deferred.resolve($window.google.maps);
			return deferred.promise;
		}
		// Callback-Funktion im window erstellen (global verfügbar), die von GoogleMaps beim Laden aufgerufen wird
		var callbackName = 'onGoogleMapsReady';
		$window[callbackName] = function() {
			// Callback-Funktion löschen
			$window[callbackName] = null;
			deferred.resolve($window.google.maps);
		};
 
		// Script-Element erstellen und das Laden des Scripts anstoßen (ins Dokument setzen)
		var script = angular.element('<script></script>');
		script.attr('src', 'https://maps.googleapis.com/maps/api/js?libraries=places&callback='+callbackName);
		$document.find('body').append(script);
 
		// Promise zurückgeben
		return deferred.promise;
	}]);
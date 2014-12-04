/**
 * Dieses Modul dient dem Verwalten von offline verfügbaren Routen in "Meine Routen".
 */
angular.module('happyHour.persistence.RoutesPersistence', [])
	.factory('RoutesPersistenceService', ['localStorageService', function(localStorageService) {
		return {
			/**
			 * Fügt eine Route zu "Meine Routen" hinzu und macht sie damit offline verfügbar.
			 * 
			 * @param {Route} route Die Route, die in "Meine Routen" gespeichert werden soll.
			 * @returns {Boolean} `true` wenn die Route hinzugefügt wurde, `false` wenn das Hinzufügen fehlgeschlagen ist
			 */
			add: function(route) {
				var routes = localStorageService.get('routes');
				routes.push(route);
				localStorageService.set('routes', routes);
			},
			/**
			 * Löscht eine Route aus "Meine Routen" und entfernt damit auch die Offlineverfügbarkeit.
			 * 
			 * @param  {Integer} routeId Der Identifikator der Route, die gelöscht werden soll
			 * @return {Boolean} Gibt `true` zurück, wenn die Route gelöscht wurde, `false` wenn sie nicht gefunden wurde.
			 */
			remove: function(routeId) {
				console.log(routeId);
			},
			/**
			 * Gibt eine Route aus "Meine Routen" zurück.
			 * 
			 * @param  {Integer} routeId Der Identifikator der Route, die zurückgegeben werden soll
			 * @return {[type]} Die angeforderte Route
			 */
			get: function(routeId) {
				console.log(routeId);
			},
			/**
			 * Gibt alle Routen aus "Meine Routen" zurück
			 * 
			 * @return {Route[]} Array aus allen Routen in "Meine Routen"
			 */
			getAll: function() {

			},
			/**
			 * Gibt zurück ob der Speicher für "Meine Routen" bereits voll ist
			 * 
			 * @return {Boolean} `true` wenn "Meine Routen" voll befüllt ist, `false` wenn nicht
			 */
			isFull: function() {

			}
		};
	}]);
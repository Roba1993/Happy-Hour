/**
 * Dieses Modul dient dem Verwalten von offline verfügbaren Routen in "Meine Routen".
 *
 * @author Dorothee Nies, Markus Thömmes
 */
angular.module('happyHour.persistence.RoutesPersistence', ['LocalStorageModule'])
	.factory('RoutesPersistenceService', ['localStorageService', function(localStorageService) {
		var routes = localStorageService.get('routes');
		// Wenn noch nichts im Storage steht, schreibe ein leeres Array hinein
		if(routes === null) {
			routes = [];
			localStorageService.set('routes', routes);
		}
		
		var service = {
			/**
			 * Fügt eine Route zu "Meine Routen" hinzu und macht sie damit offline verfügbar.
			 * 
			 * @author Dorothee Nies, Markus Thömmes
			 * @param {Route} route Die Route, die in "Meine Routen" gespeichert werden soll.
			 * @return {String|Boolean} die für das Objekt erzeugte id, false wenn das Einfügen fehlgeschlagen ist
			 */
			add: function(route) {
				if (service.isFull()){
					return false;
				}
				var object = {
					timestamp: new Date(),
					route: route
				};
				var id = CryptoJS.MD5(JSON.stringify(object));
				object.id = id;

				routes.push(object);
				localStorageService.set('routes', routes);
				return id;
			},
			/**
			 * Löscht eine Route aus "Meine Routen" und entfernt damit auch die Offlineverfügbarkeit.
			 * 
			 * @author Dorothee Nies
			 * @param  {String} routeId Der Identifikator der Route, die gelöscht werden soll
			 * @return {Boolean} Gibt `true` zurück, wenn die Route gelöscht wurde, `false` wenn sie nicht gefunden wurde.
			 */
			remove: function(routeId) {
				var routeToDelete = null;
				for (var i = 0; i < routes.length; i++){
					if (routes[i].id === routeId){
						routeToDelete = routes[i];
					}
				}
				if (routeToDelete === null){
					return false;
				}
				else {
					routes = _.without(routes, routeToDelete);
					localStorageService.set('routes', routes);
					return true;
				}
			},
			/**
			 * Gibt eine Route aus "Meine Routen" zurück.
			 * 
			 * @author Dorothee Nies
			 * @param  {Integer} routeId Der Identifikator der Route, die zurückgegeben werden soll
			 * @return {[LocalRoute]} Die angeforderte Route
			 */
			get: function(routeId) {
				var routeToReturn = null;
				for (var i = 0; i < routes.length; i++){
					if (routes[i].id === routeId){
						routeToReturn = routes[i];
					}
				}
				return routeToReturn;
			},
			/**
			 * Gibt alle Routen aus "Meine Routen" zurück
			 * 
			 * @author Dorothee Nies
			 * @return {LocalRoute[]} Array aus allen Routen in "Meine Routen"
			 */
			getAll: function() {
				return routes;
			},
			/**
			 * Gibt zurück ob der Speicher für "Meine Routen" bereits voll ist
			 * 
			 * @author Dorothee Nies
			 * @return {Boolean} `true` wenn "Meine Routen" voll befüllt ist, `false` wenn nicht
			 */
			isFull: function() {
				var maxRoutes = 10;
				if (routes.length === maxRoutes){
					return true;
				}
				else {
					return false;
				}
			}
		};
		return service;
	}]);
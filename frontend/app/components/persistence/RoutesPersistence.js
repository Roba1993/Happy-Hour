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
			 * @return {String} die für das Objekt erzeugte id
			 */
			add: function(route) {
				if (service.isFull()){
					return false;
				}
				// LocalRoute Objekt erstellen
				var object = {
					timestamp: new Date(),
					route: route
				};
				// Objekt hashen und Hash als ID setzen
				var id = CryptoJS.MD5(JSON.stringify(object)).toString(CryptoJS.enc.Hex);
				object.id = id;

				// Route hinzufügen und persistieren
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
				// zu löschende Route suchen
				for (var i = 0; i < routes.length; i++){
					if (routes[i].id === routeId){
						routeToDelete = routes[i];
					}
				}
				// Wenn keine Route gefunden wurde, false zurückgeben
				if (routeToDelete === null){
					return false;
				}
				else {
					// Route aus Array entfernen und persistieren
					routes = _.without(routes, routeToDelete);
					localStorageService.set('routes', routes);
					return true;
				}
			},
			/**
			 * Gibt eine Route aus "Meine Routen" zurück.
			 * 
			 * @author Dorothee Nies
			 * @param  {String} routeId Der Identifikator der Route, die zurückgegeben werden soll
			 * @return {LocalRoute} Die angeforderte Route
			 */
			get: function(routeId) {
				var routeToReturn = null;
				// Zurückzugebende Route suchen
				for (var i = 0; i < routes.length; i++){
					if (routes[i].id === routeId){
						routeToReturn = routes[i];
					}
				}
				// Klon der Route zurückgeben
				return _.cloneDeep(routeToReturn);
			},
			/**
			 * Gibt alle Routen aus "Meine Routen" zurück
			 * 
			 * @author Dorothee Nies
			 * @return {LocalRoute[]} Array aus allen Routen in "Meine Routen"
			 */
			getAll: function() {
				return _.cloneDeep(routes);
			},
			/**
			 * Gibt zurück ob der Speicher für "Meine Routen" bereits voll ist
			 * 
			 * @author Dorothee Nies
			 * @return {Boolean} `true` wenn "Meine Routen" voll befüllt ist, `false` wenn nicht
			 */
			isFull: function() {
				var maxRoutes = 10;
				if (routes.length >= maxRoutes){
					return true;
				}
				else {
					return false;
				}
			}
		};
		return service;
	}]);
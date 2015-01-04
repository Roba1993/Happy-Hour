/**
 * Dieses Modul dient dem Verwalten des App-Statuses.
 *
 * @author Dorothee Nies, Markus Thömmes
 */
angular.module('happyHour.persistence.AppStatusPersistence', ['LocalStorageModule'])
	.factory('AppStatusPersistenceService', ['localStorageService', function(localStorageService) {
			var status = localStorageService.get('appStatus');
			if(status === null) {
				status = {
					currentRoute: null,
					appPath: null
				};
			}
			var service =  {
				/**
				 * Speichert die aktuelle Route auf dem Gerät des Nutzers
				 * 
				 * @author Dorothee Nies
				 * @param {Route} route Die aktuell bearbeitete Route
				 */
				setRoute: function(route) {
					status.currentRoute = route;
					localStorageService.set('appStatus', status);
				},
				/**
				 * Gibt die zuletzt gespeicherte Route der Anwendung zurück
				 * 
				 * @author Dorothee Nies
				 * @returns {Route} Die zuletzt gespeicherte Route
				 */
				getRoute: function() {
					return status.currentRoute;
				},
				/**
				 * Speichert den aktuellen Pfad der Anwendung
				 *
				 * @author Markus Thömmes
				 * @param {String} path der aktuelle Pfad in der Anwendung
				 */
				setPath: function(path) {
					status.appPath = path;
					localStorageService.set('appStatus', status);
				},
				/**
				 * Gibt den zuletzt gespeicherten Pfad in der Anwendung zurück
				 *
				 * @author Markus Thömmes
				 * @return {String} der zuletzt gespeicherte Pfad in der Anwendung
				 */
				getPath: function() {
					return status.appPath;
				}
			};

			return service;
	 }]);
/**
 * Dieses Modul dient dem Verwalten des App-Statuses.
 *
 * @author Dorothee Nies, Markus Thömmes
 */
angular.module('happyHour.persistence.AppStatusPersistence', ['LocalStorageModule'])
	.factory('AppStatusPersistenceService', ['localStorageService', function(localStorageService) {
			var service =  {
				/**
				 * Speichert den Anwendungsstatus auf dem Gerät des Nutzers.
				 * 
				 * @author Dorothee Nies
				 * @param {Status} status Der aktuelle Status der Anwendung
				 */
				setStatus: function(status) {
					localStorageService.set('appStatus', status);
				},
				/**
				 * Gibt den zuletzt gespeicherten Status der Anwendung zurück.
				 * 
				 * @author Dorothee Nies
				 * @returns {Status} Der Status der Anwendung
				 */
				getStatus: function() {
					return localStorageService.get('appStatus');
				}
			};

			return service;
	 }]);
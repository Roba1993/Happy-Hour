/**
 * Dieses Modul dient dem Verwalten des App-Statuses.
 */
angular.module('happyHour.persistence.AppStatusPersistence', ['LocalStorageModule'])
  .factory('AppStatusService', ['localStorageService', function(localStorageService) {
      var service =  {
        /**
         * Speichert den Anwendungsstatus auf dem Gerät des Nutzers.
         * 
         * @author Dorothee Nies
         * @param {Status} status Der aktuelle Status der Anwendung
         */
        setStatus: function(status) {
          console.log(status);
          console.log(localStorageService);
        },
        /**
         * Gibt den zuletzt gespeicherten Status der Anwendung zurück.
         * 
         * @author Dorothee Nies
         * @returns {Status} Der Status der Anwendung
         */
        getStatus: function() {

        }
      };

      return service;
   }]);
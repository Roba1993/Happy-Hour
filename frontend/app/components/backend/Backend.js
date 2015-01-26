/**
 * Der Backend Service dient als Schnittstelle zwischen Frontend und Backend und liefert vom
 * Backend bereitgestellte Daten an das Frontend.
 *
 * @author Kim Rinderknecht, Daniel Reichert
 */
angular.module('happyHour.backend.Backend', [])
	.factory('BackendService', ['$http', '$q', function($http, $q) {
		var baseUrl = 'http://localhost:8080';
		var service = {
			/**
			 * Gibt alle Bars zurück, die den übergeben Parametern entsprechen.
			 *
			 * @author Daniel Reichert, Kim Rinderknecht
			 * @param  {Location} location latitude und longitude in einem Objekt.
			 * @param  {Double} radius Der Radius, der über die GUI vom User eingegeben wurde.
			 * @param  {Integer} weekday Der Wochentag, der über die GUI vom User eingegeben wurde.
			 * @return {Promise(Bar[])} Gibt ein Array mit allen passenden Bars zurück.
			 */
			getBars: function(location, radius, weekday) {
				var latitude = location.latitude;
				var longitude = location.longitude;

				var url = baseUrl+'/bars?latitude=' + latitude + '&longitude=' + longitude + '&radius=' + radius + '&weekday=' + weekday;

				var promise = $http({method: 'GET', url: url});
				var deferred = $q.defer();

				promise.then(function(data){ //data wird befüllt mit der Rückgabe
					//Rückgabe an den Serviceanfragenden
					deferred.resolve(data.data.data);
				});
				return deferred.promise;

			},
			/**
			 * Speichert Meldungen von Bars im Backend.
			 *
			 * @author Daniel Reichert, Kim Rinderknecht
			 * @param  {Integer} barId ID der Bar, für die eine Meldung gespeichert werden soll.
			 * @param  {String} description Text der Meldung.
			 * @return {Promise(Boolean)} Gibt `true` zurück, wenn die Meldung erfolgreich gespeichert wurde und `false` wenn nicht.
			 */
			reportData: function(barId, description) {
				var url = baseUrl+'/bars/' + barId + '/reports';

				var promise = $http({method: 'POST', url: url, data: description});
				var deferred = $q.defer();

				promise.then(function(data){ //data wird befüllt mit der Rückgabe
					//Rückgabe an den Serviceanfragenden
					deferred.resolve(data.data.data);
				});
				return deferred.promise;

			},
			/**
			 * Speichert eine Route im Backend und übergibt einen Link zu der erstellten Route
			 *
			 * @author Daniel Reichert, Kim Rinderknecht
			 * @param  {Route} route JSON Route Objekt, das direkt ins Backend weitergeleitet werden kann.
			 * @return {Promise(String)} Übergibt einen Hash zu der erstellten Route
			 */
			saveRoute: function(route) {

				var url = baseUrl+'/routes';

				var promise = $http({method: 'POST', url: url, data: route});
				var deferred = $q.defer();

				promise.then(function(data){ //data wird befüllt mit der Rückgabe
					//Rückgabe an den Serviceanfragenden
					deferred.resolve(data.data.data);
				});
				return deferred.promise;
			},
      /**
			 * Speichert eine Happy Hour im Backend und übergibt einen Boolean, ob das Schreiben erfolgreich war oder nicht.
			 *
			 * @author Daniel Reichert, Kim Rinderknecht
			 * @param  {HappyHour} happy JSON Happy-Hour Objekt, das direkt ins Backend weitergeleitet werden kann.
			 * @param  {Integer} barId Die Bar ID, für die die Happy Hour gilt.
			 * @param {String} admin Adminname
			 * @param {String} adminpw Adminpasswort
			 * @return {Promise(Boolean)} Gibt `true` zurück, wenn die Happy Hour erfolgreich gespeichert wurde und `false` wenn nicht.
			 */
			saveHappy: function(happy, barId, admin, adminpw) {
				var url = baseUrl+'/'+barId+'/hour?admin='+admin+'&adminpw='+adminpw;

				var promise = $http({method: 'POST', url: url, data: happy});
				var deferred = $q.defer();

				promise.then(function(data){ //data wird befüllt mit der Rückgabe
					//Rückgabe an den Serviceanfragenden
					deferred.resolve(data.data.data);
				});
				return deferred.promise;
			},
			/**
			 * Gibt eine Route mit dem angeforderten Hash zurück.
			 *
			 * @author Daniel Reichert, Kim Rinderknecht
			 * @param  {String} hash Ein Hashwert, der zuvor beim Speichern einer Route zurückgegeben wurde.
			 * @return {Promise(Route)} Ein Routen Objekt, mit allen darin enthaltenen Bars.
			 */
			getRoute: function(hash) {

				var url = baseUrl+'/routes/' + hash;

				var promise = $http({method: 'GET', url: url});
				var deferred = $q.defer();

				promise.then(function(data){ //data wird befüllt mit der Rückgabe
					//Rückgabe an den Serviceanfragenden
					deferred.resolve(data.data.data);
				});
				return deferred.promise;

			},
			/**
			 * Gibt alle Toprouten zurück.
			 *
			 * @author Daniel Reichert, Kim Rinderknecht
			 * @return {Promise(Route[])} Alle Toprouten mit allen darin enthaltenen Bars.
			 */
			getToproutes: function() {

				var url = baseUrl+'/toproutes';

				var promise = $http({method: 'GET', url: url});
				var deferred = $q.defer();

				promise.then(function(data){ //data wird befüllt mit der Rückgabe
					//Rückgabe an den Serviceanfragenden
					deferred.resolve(data.data.data);
				});
				return deferred.promise;

			},
			/**
			 * Gibt alle gemeldeten Bars mit den zugehörigen Meldungen zurück.
			 *
			 * @author Daniel Reichert, Kim Rinderknecht
			 * @param {String} admin Adminname
			 * @param {String} adminpw Adminpasswort
			 * @return {Promise(Report[])} Alle vorliegenden Meldungen zu allen Bars.
			 */
			getReports: function(admin, adminpw) {

				var url = baseUrl+'/bars/reports?admin='+admin+'&adminpw='+adminpw;

				var promise = $http({method: 'GET', url: url});
				var deferred = $q.defer();

				promise.then(function(data){ //data wird befüllt mit der Rückgabe
					//Rückgabe an den Serviceanfragenden
					deferred.resolve(data.data.data);
				});
				return deferred.promise;

			},
		 /**
			* Loescht eine bestimmte Meldung zu einer Bar.
			*
			* @author Daniel Reichert, Kim Rinderknecht
			* @param {Integer} barId Die ID der Bar mit der Fehlermeldung
			* @param {Integer} reportId Die ID der zu loeschenden Meldung
			* @param {String} admin Adminname
			* @param {String} adminpw Adminpasswort
			* @return {Promise(Boolean)} Gibt `true` zurück, wenn die Meldung erfolgreich gelöscht wurde und `false` wenn nicht.
			*/
			delReport: function(barId, reportId, admin, adminpw) {

				var url = baseUrl+'/bars/'+barId+'/report?reportid='+reportId+'&admin='+admin+'&adminpw='+adminpw;

				var promise = $http({method: 'DELETE', url: url});
				var deferred = $q.defer();

				promise.then(function(data){ //data wird befüllt mit der Rückgabe
					//Rückgabe an den Serviceanfragenden
					deferred.resolve(data.data.data);
				});
				return deferred.promise;

			},
		 /**
			* Löscht alle zu einer Bar vorhandene Meldungen.
			*
			* @author Daniel Reichert, Kim Rinderknecht
			* @param {Integer} barId Die ID der Bar mit den Meldungen
			* @param {String} admin Adminname
			* @param {String} adminpw Adminpasswort
			* @return {Promise(Boolean)} Gibt `true` zurück, wenn alle Meldungen zu einer Bar erfolgreich gelöscht wurden und `false` wenn nicht.
			*/
			delReports: function(barId, admin, adminpw) {

				var url = baseUrl+'/reports/'+barId+'?admin='+admin+'&adminpw='+adminpw;

				var promise = $http({method: 'DELETE', url: url});
				var deferred = $q.defer();

				promise.then(function(data){ //data wird befüllt mit der Rückgabe
					//Rückgabe an den Serviceanfragenden
					deferred.resolve(data.data.data);
				});
				return deferred.promise;

			},
		 /**
			* Löscht die Happy Hour Angabe mit der übergebenen ID.
			*
			* @author Daniel Reichert, Kim Rinderknecht
			* @param {Integer} hourId Die ID der zu löschenden HappyHour
			* @param {String} admin Adminname
			* @param {String} adminpw Adminpasswort
			* @return {Promise(Boolean)} Gibt `true` zurück, wenn die Happy Hour erfolgreich gelöscht wurde und `false` wenn nicht.
			*/
			deleteHappy: function(hourId, admin, adminpw) {

				var url = baseUrl+'/delHour/'+hourId+'?admin='+admin+'&adminpw='+adminpw;

				var promise = $http({method: 'DELETE', url: url});
				var deferred = $q.defer();

				promise.then(function(data) { //data wird befüllt mit der Rückgabe
					//Rückgabe an den Serviceanfragenden
					deferred.resolve(data.data.data);
				});
				return deferred.promise;
			}
		};

		return service;
	}]);

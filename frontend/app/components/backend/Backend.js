/**
 * Der Backend Service dient als Schnittstelle zwischen Frontend und Backend und liefert vom
 * Backend bereitgestellte Daten an das Frontend.
 *
 * @author Kim Rinderknecht, Daniel Reichert
 */
angular.module('happyHour.backend.Backend', [])
	.factory('BackendService', ['$http', '$q', function($http, $q) {
		var service = {
			/**
			 * Gibt alle Bars zurück, die den übergeben Parametern entsprechen.
			 *
			 * @author Daniel Reichert, Kim Rinderknecht
			 * @param  {Location} location latitude und longitude in einem Objekt.
			 * @param  {Double} radius Der Radius, der über die GUI vom User eingegeben wurde.
			 * @param  {Integer} weekday Der Wochentag, der über die GUI vom User eingegeben wurde.
			 * @return {Promise(Bar[])} Array mit allen passenden Bars
			 */
			getBars: function(location, radius, weekday) {
				console.log(location);
				console.log(radius);
				console.log(weekday);

                var latitude = location.data.latitude;
                var longitude = location.data.longitude;

                var url = 'http://localhost:8080/bars?lat=' + latitude + '&long=' + longitude + '&radius=' + radius + '&weekday=' + weekday;

                console.log(url);

                var promise = $http({method: 'GET', url: url});
                var deferred = $q.defer();

                promise.then(
                    function(data){ //data wird befüllt mit der Rückgabe

                    deferred.resolve(data.data
                        //Rückgabe an den Serviceanfragenden
                    );
                    }
                );
                return deferred.promise;

			},
			/**
			 * Speichert Meldungen von Bars im Backend.
			 *
			 * @author Daniel Reichert, Kim Rinderknecht
			 * @param  {Integer} barId ID der Bar, für die eine Meldung gespeichert werden soll.
			 * @param  {String} description Text der Meldung.
			 * @return {Promise(Boolean)} Gibt `true` zurück wenn die Meldung erfolgreich gespeichert wurde und `false` wenn nicht.
			 */
			reportData: function(barId, description) {
				console.log(barId);
				console.log(description);

				var url = 'http://localhost:8080/bars/' + barId.data.barId + '/reports/';

				console.log(url);

				var promise = $http({method: 'POST', url: url, data: description});
				var deferred = $q.defer();

				promise.then(
								function(data) { //data wird befüllt mit der Rückgabe
									deferred.resolve(data.data
												//Rückgabe an den Serviceanfragenden, ob das Melden erfolgreich war oder nicht.
									);
								}
				);
				return deferred. promise;

			},
			/**
			 * Speichert Routen im Backend und übergibt einen Link zu der erstellten Route
			 *
			 * @author Daniel Reichert, Kim Rinderknecht
			 * @param  {Route} route JSON Route Objekt, das direkt ins Backend weitergeleitet werden kann.
			 * @return {Promise(String)} Übergibt einen Hash zu der erstellten Route
			 */
			saveRoute: function(route) {
				console.log(route);

				var url = 'http://localhost:8080/routes/';

				var promise = $http({method: 'POST', url: url, data: route});
				var deferred = $q.defer();

				promise.then(
					function(data) { //data wird befüllt mit der Rückgabe
						deferred.resolve(data.data
										//Rückgabe an den Serviceanfragenden in Form eines Hashes der gespeicherten Route
						);
					}
				);
				return deferred.promise;
			},
			/**
			 * Gibt eine Route mit dem angeforderten hashzurück.
			 *
			 * @author Daniel Reichert, Kim Rinderknecht
			 * @param  {String} hash Ein Hashwert, der zuvor beim Speichern einer Route zurückgegeben wurde.
			 * @return {Promise(Route)} Route mit allen darin enthaltenen Bars.
			 */
			getRoute: function(hash) {
				console.log(hash);

                var url = 'http://localhost:8080/routes/' + hash;

                var promise = $http({method: 'GET', url: url});
                var deferred = $q.defer();

                promise.then(
                    function(data){ //data wird befüllt mit der Rückgabe

                    deferred.resolve(data.data
                        //Rückgabe an den Serviceanfragenden
                    );
                    }
                );
                return deferred.promise;

			},
			/**
			 * Gibt alle Toprouten zurück.
			 *
			 * @author Daniel Reichert, Kim Rinderknecht
			 * @return {Promise(Route[])} Alle Toprouten mit allen darin enthaltenen Bars.
			 */
			getToproutes: function() {

				var url = 'http://localhost:8080/toproutes/';

				var promise = $http({method: 'GET', url: url});
				var deferred = $q.defer();

				promise.then(
					function(data){ //data wird befüllt mit der Rückgabe

						deferred.resolve(data.data
							//Rückgabe an den Serviceanfragenden in Form eines Arrays mit den Top Routen.
						);
					}
				);
				return deferred.promise;

			}
		};

		return service;
	}]);

/* Nutzung: 
 * 1. Das Modul 'googleMaps.Loader' in der app.js als Abhängigkeit angeben
 * 2. Den Provider 'googleMapsApi' im Controller als Abhängigkeit angeben
 * 3. Den Promise nutzen
 */
angular.module('googleMaps.Loader', [])
    .factory('googleMapsApi', ['$q', function($q) {
        // Promise erstellen
        var deferred = $q.defer();
        // Wenn die API schon geladen ist, das Promise sofort resolven und die Funktion abbrechen (return des Promises)
        if (angular.isDefined(window.google) && angular.isDefined(window.google.maps)) {
            deferred.resolve(window.google.maps);
            return deferred.promise;
        }
        // Callback-Funktion im window erstellen (global verfügbar), die von GoogleMaps beim Laden aufgerufen wird
        var callbackName = 'onGoogleMapsReady';
        window[callbackName] = function() {
            // Callback-Funktion löschen
            window[callbackName] = null;
            deferred.resolve(window.google.maps);
        };

        // Script-Element erstellen und das Laden des Scripts anstoßen (ins Dokument setzen)
        var script = document.createElement('script');
        script.src = 'https://maps.googleapis.com/maps/api/js?callback='+callbackName;
        document.body.appendChild(script);

        // Promise zurückgeben
        return deferred.promise;
    }]);
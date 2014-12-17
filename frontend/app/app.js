// Definiere anwendungsweite Abhängigkeiten
angular.module('happyHour', [
  'ngRoute',
  'happyHour.persistence.RoutesPersistence',
  'happyHour.persistence.AppStatusPersistence',
  'happyHour.backend.Backend',
  'happyHour.algorithm.RouteGenerator',
  'happyHour.map.MapDirective',

  'happyHour.views.currentRoute',
  'happyHour.views.currentRouteMap',
  'happyHour.views.localRoutes',
  'happyHour.views.topRoutes'
])
.config(['$routeProvider', 'localStorageServiceProvider', function($routeProvider, localStorageServiceProvider) {
  // Wenn keine passende Route gefunden wird, leite auf currentRoute weiter
  $routeProvider.otherwise({redirectTo: '/currentRoute'});

  // LocalStorage-Prefix setzen
  localStorageServiceProvider.setPrefix('happyHour');
}]);
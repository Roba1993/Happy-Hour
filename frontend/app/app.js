// Definiere anwendungsweite Abhängigkeiten
angular.module('happyHour', [
  'ngRoute',
  'happyHour.persistence.RoutesPersistence',
  'happyHour.persistence.AppStatusPersistence',
  'happyHour.backend.Backend',
  'happyHour.algorithm.RouteGenerator',
  'happyHour.map.MapDirective',
  'myApp.view1',
  'myApp.view2',
  'myApp.map',

  'happyHour.views.currentRoute',
  'happyHour.views.currentRouteMap',
  'happyHour.views.localRoutes',
  'happyHour.views.topRoutes'
])
.config(['$routeProvider', 'localStorageServiceProvider', function($routeProvider, localStorageServiceProvider) {
  // Wenn keine passende Route gefunden wird, leite auf view1 weiter
  $routeProvider.otherwise({redirectTo: '/view1'});

  // LocalStorage-Prefix setzen
  localStorageServiceProvider.setPrefix('happyHour');
}]);
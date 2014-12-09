// Definiere anwendungsweite Abhängigkeiten
angular.module('myApp', [
  'ngRoute',
  'happyHour.persistence.RoutesPersistence',
  'happyHour.persistence.AppStatusPersistence',
  'happyHour.backend.Backend',
  'happyHour.algorithm.RouteGenerator',
  'happyHour.map.MapDirective',
  'myApp.view1',
  'myApp.view2'
])
.config(['$routeProvider', 'localStorageServiceProvider', function($routeProvider, localStorageServiceProvider) {
  // Wenn keine passende Route gefunden wird, leite auf view1 weiter
  $routeProvider.otherwise({redirectTo: '/view1'});
  localStorageServiceProvider.setPrefix('happyHour');
}]);
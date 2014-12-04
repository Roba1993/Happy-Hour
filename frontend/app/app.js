// Definiere anwendungsweite Abhängigkeiten
angular.module('myApp', [
  'ngRoute',
  'LocalStorageModule',
  'happyHour.persistence.RoutesPersistence',
  'myApp.view1',
  'myApp.view2',
  'myApp.map'
])
.config(['$routeProvider', 'localStorageServiceProvider', function($routeProvider, localStorageServiceProvider) {
  // Wenn keine passende Route gefunden wird, leite auf view1 weiter
  $routeProvider.otherwise({redirectTo: '/view1'});
  localStorageServiceProvider.setPrefix('happyHour');
}]);
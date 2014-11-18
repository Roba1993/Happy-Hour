'use strict';

// Definiere anwendungsweite Abhängigkeiten
angular.module('myApp', [
  'ngRoute',
  'myApp.view1',
  'myApp.view2'
]).
config(['$routeProvider', function($routeProvider) {
  // Wenn keine passende Route gefunden wird, leite auf view1 weiter
  $routeProvider.otherwise({redirectTo: '/view1'});
}]);

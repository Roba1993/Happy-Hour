'use strict';

// Definiere das Modul und seine Abhängigkeiten
angular.module('myApp.view2', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  // wenn #/view2 aufgerufen wird
  $routeProvider.when('/view2', {
    // lade view2.html
    templateUrl: 'view2/view2.html',
    // instanziiere den Controller View2Ctrl
    controller: 'View2Ctrl'
  });
}])

// Definiere den Controller
.controller('View2Ctrl', ['$scope', function($scope) {
  // Setze einen Wert in den Scope (um ihn im view2.html anzuzeigen)
  $scope.name = "Markus";
}]);
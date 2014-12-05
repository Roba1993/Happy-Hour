// Definiere das Modul und seine Abhängigkeiten
angular.module('myApp.view1', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  // wenn #/view1 aufgerufen wird
  $routeProvider.when('/view1', {
    // lade view1.html
    templateUrl: 'view1/view1.html',
    // instanziiere den Controller View1Ctrl
    controller: 'View1Ctrl'
  });
}])

// Definiere den Controller
.controller('View1Ctrl', ['$scope', 'RoutesPersistenceService', 'AppStatusPersistenceService', function($scope, RoutesPersistenceService, AppStatusPersistenceService) {
  // Setze einen Wert in den Scope (um ihn im view1.html anzuzeigen)

  $scope.barSchalter = false;
  $scope.bars = [];
  $scope.i = 0;
  $scope.barClicked = function() {
  	if($scope.i < $scope.bars.length-1) {
  		$scope.i++;
  	}
  	else {
  		$scope.i = 0;
  	}
  };
  var bar1 = {
	id: 1,
  	name: 'Monobar',
  	description: 'Eine tolle Bar',
  	rating: 4
  };
  console.log(AppStatusPersistenceService);
  console.log(RoutesPersistenceService);
  $scope.bars.push(bar1);
  var bar2 = {
	id: 2,
  	name: 'Tequila Bar',
  	description: 'Eine tolle Bar mit tollen Getränken',
  	rating: 5
  };
  $scope.bars.push(bar2);
  var bar3 = {
	id: 3,
  	name: 'Lutscher Bar',
  	description: 'Eine Bar mit hartem Lörres',
  	rating: 1
  };
  $scope.bars.push(bar3);

}]);
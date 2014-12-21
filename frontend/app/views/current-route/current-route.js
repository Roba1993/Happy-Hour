angular.module('happyHour.views.currentRoute', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/currentRoute', {
    templateUrl: 'views/current-route/current-route.html',
    controller: 'currentRouteController'
  });
}])

.controller('currentRouteController', 
['$scope', 'BackendService', 'RouteGeneratorService', 'RoutesPersistenceService', 'AppStatusPersistenceService', 
function($scope, BackendService, RouteGeneratorService, RoutesPersistenceService, AppStatusPersistenceService) {
	// Aktuellen Pfad persistieren
	AppStatusPersistenceService.setPath('/currentRoute');

	// Aktuelle Route aus dem AppStatus auslesen
	$scope.route = AppStatusPersistenceService.getRoute();
	if($scope.route === null) {
		$scope.route = {};
		$scope.route.options = {};
	}

	// Die Route im AppStatus bei jedem Ändern des Routenobjekts aktualisieren
	$scope.$watch('route', function(route) {
		AppStatusPersistenceService.setRoute(route);
		console.log(route);
	}, true);
	
	// Es können nur die Details einer einzelnen Bar betrachtet werden
	$scope.openFrameIndex = -1;
	$scope.frameClicked = function(index) {
		if(index === $scope.openFrameIndex) {
			$scope.openFrameIndex = -1;
		}
		else {
			$scope.openFrameIndex = index;
		}
	};
	
	// Eine Route auf dem Gerät persistieren
	$scope.saveRoute = function() {
		RoutesPersistenceService.add($scope.route);
	};


	// start und endTime defaults binden
	if($scope.route.options.startTime !== undefined && $scope.route.options.endTime !== undefined) {
		$scope.routeTime = [$scope.route.options.startTime, $scope.route.options.endTime];
	}
	else {
		$scope.routeTime = ['20:00', '03:00'];
	}
	// Ausgabe des Sliders in das Options Format umwandeln
	$scope.$watch('routeTime', function(routeTime) {
		$scope.route.options.startTime = routeTime[0];
		$scope.route.options.endTime = routeTime[1];
	});
}]);
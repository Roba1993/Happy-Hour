angular.module('happyHour.views.currentRoute', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/currentRoute', {
    templateUrl: 'views/current-route/current-route.html',
    controller: 'currentRouteController'
  });
}])

.controller('currentRouteController', ['$scope', 'BackendService', 'RouteGeneratorService', 'RoutesPersistenceService', function($scope, BackendService, RouteGeneratorService, RoutesPersistenceService) {
	BackendService.getBars({latitude: 1.1, longitude:1.2}, 1, 6).then(function(bars) {
		var route = RouteGeneratorService.createRoute(bars, {});
		$scope.route = route;
	});
	
	$scope.openFrameIndex = -1;
	$scope.frameClicked = function(index) {
		if(index === $scope.openFrameIndex) {
			$scope.openFrameIndex = -1;
		}
		else {
			$scope.openFrameIndex = index;
		}
	};
	
	$scope.saveRoute = function(){
		console.log('hiiiier');
		RoutesPersistenceService.add($scope.route);
	};
}]);
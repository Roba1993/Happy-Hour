angular.module('happyHour.views.currentRoute', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/currentRoute', {
    templateUrl: 'views/current-route/current-route.html',
    controller: 'currentRouteController'
  });
}])

.controller('currentRouteController', ['$scope', 'BackendService', 'RouteGeneratorService', function($scope, BackendService, RouteGeneratorService) {
	BackendService.getBars({latitude: 1.1, longitude:1.2}, 1, 6).then(function(bars) {
			var route = RouteGeneratorService.createRoute(bars, {});
			$scope.route = route;
		});
}]);
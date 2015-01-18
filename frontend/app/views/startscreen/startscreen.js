angular.module('happyHour.views.startScreen', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/startScreen', {
    templateUrl: 'views/startscreen/startscreen.html',
    controller: 'startScreenController'
  });
}])

.controller('startScreenController', ['$scope', 'AppStatusPersistenceService', '$location', 'BackendService', 'RouteGeneratorService',
function($scope, AppStatusPersistenceService, $location, BackendService, RouteGeneratorService) {
	// Routenstandardwerte
	var routeOptions = {
		radius: 2.5,
		location: {longitude:9.1833333, latitude: 48.7666667},
		startTime: '18:00',
		endTime: '23:00',
		stayTime: 1,
		weekday: 5
	};

	$scope.buttonClicked = function() {
		BackendService.getBars(routeOptions.location, routeOptions.radius, routeOptions.weekday).then(function(bars) {
			console.log(bars);
			var route = RouteGeneratorService.createRoute(bars, routeOptions);
			AppStatusPersistenceService.setRoute(route);
			$location.path('/currentRoute');
		});
	};
}]);
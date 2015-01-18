angular.module('happyHour.views.testview', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/testview', {
    templateUrl: 'views/testview/testview.html',
    controller: 'testViewController'
  });
}])

.controller('testViewController', ['$scope', 'RouteGeneratorService', 'BackendService', function($scope, RouteGeneratorService, BackendService) {
	$scope.test = 'testViewController!!';

	BackendService.getBars({longitude:9.1833333, latitude: 48.7666667}, 2.5, 3).then(function(bars) {
		console.log(bars);
		var routeOptions = {
			stayTime: 1,
			radius: 2.5,
			location: {longitude:9.1833333, latitude: 48.7666667},
			startTime: '17:00',
			endTime: '22:00'
		};
		var route = RouteGeneratorService.createRoute(bars, routeOptions);

		console.log(route);
	});
}]);
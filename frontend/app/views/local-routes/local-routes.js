angular.module('happyHour.views.localRoutes', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/localRoutes', {
    templateUrl: 'views/local-routes/local-routes.html',
    controller: 'localRoutesController'
  });
}])

.controller('localRoutesController', ['$scope', function($scope) {
	var localRoutes = [];
	var localRoute1 = {
		id: 'abcdefg',
		timestamp: new Date(),
		route: {
			name: 'Tour1',
			options: {
				weekday: 5
			}
		}
	};
	localRoutes.push(localRoute1);
	$scope.localRoutes = localRoutes;
}]);
angular.module('happyHour.views.topRoutes', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/topRoutes', {
    templateUrl: 'views/top-routes/top-routes.html',
    controller: 'topRoutesController'
  });
}])

.controller('topRoutesController', ['$scope', 'AppStatusPersistenceService', '$location', function($scope, AppStatusPersistenceService, $location) {
	AppStatusPersistenceService.setPath('/topRoutes');

	$scope.routes = [];
	$scope.openRoute = function(index){
		AppStatusPersistenceService.setRoute($scope.routes[index]);
		$location.path('/currentRoute');
	};
}]);
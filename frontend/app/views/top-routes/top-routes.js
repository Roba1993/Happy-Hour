angular.module('happyHour.views.topRoutes', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/topRoutes', {
    templateUrl: 'views/top-routes/top-routes.html',
    controller: 'topRoutesController'
  });
}])

.controller('topRoutesController', ['$scope', 'AppStatusPersistenceService', '$location', 'BackendService', function($scope, AppStatusPersistenceService, $location, BackendService) {
	AppStatusPersistenceService.setPath('/topRoutes');

	$scope.routes = [];
	// Toprouten aus dem Backend auslesen
	
	$scope.showLoading = true;
	BackendService.getToproutes().then(function(routes) {
		$scope.routes = routes;
		$scope.showLoading = false;
	});

	// Route öffnen
	$scope.openRoute = function(index){
		AppStatusPersistenceService.setRoute($scope.routes[index]);
		$location.path('/currentRoute');
	};
}]);
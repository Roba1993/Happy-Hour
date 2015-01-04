angular.module('happyHour.views.localRoutes', ['ngRoute', 'happyHour.persistence.RoutesPersistence'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/localRoutes', {
    templateUrl: 'views/local-routes/local-routes.html',
    controller: 'localRoutesController'
  });
}])

.controller('localRoutesController', 
['$scope', 'AppStatusPersistenceService', 'RoutesPersistenceService', 
function($scope, AppStatusPersistenceService, RoutesPersistenceService) {
	AppStatusPersistenceService.setPath('/localRoutes');
	/*var localRoutes = [];
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
	
	var localRoute2 = {
		id: 'abcdefgfg',
		timestamp: new Date(),
		route: {
			name: 'Tour2',
			options: {
				weekday: 5
			}
		}
	};
	localRoutes.push(localRoute2);*/
	
	$scope.localRoutes = RoutesPersistenceService.getAll();
	$scope.removeRoute = function(routeId) {
		RoutesPersistenceService.remove(routeId);
		$scope.localRoutes = RoutesPersistenceService.getAll();
	};
}]);
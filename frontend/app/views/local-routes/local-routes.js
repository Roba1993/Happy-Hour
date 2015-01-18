angular.module('happyHour.views.localRoutes', ['ngRoute', 'happyHour.persistence.RoutesPersistence'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/localRoutes', {
    templateUrl: 'views/local-routes/local-routes.html',
    controller: 'localRoutesController'
  });
}])

.controller('localRoutesController', 
['$scope', 'AppStatusPersistenceService', 'RoutesPersistenceService', '$location',
function($scope, AppStatusPersistenceService, RoutesPersistenceService, $location) {
	AppStatusPersistenceService.setPath('/localRoutes');
	
	// Alle Routen aus dem AppStatus auslesen	
	$scope.localRoutes = RoutesPersistenceService.getAll();
	
	// Gewählte Route aus den gespeicherten Routen löschen
	$scope.removeRoute = function(routeId) {
		RoutesPersistenceService.remove(routeId);
		$scope.localRoutes = RoutesPersistenceService.getAll();
	};
	
	// Gewählte Route in der currentRoute-Ansicht öffnen
	$scope.openRoute = function(routeId){
		var localRouteToOpen = RoutesPersistenceService.get(routeId);
		AppStatusPersistenceService.setRoute(localRouteToOpen.route);
		$location.path('/currentRoute');
	};
}]);
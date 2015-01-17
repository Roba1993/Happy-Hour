angular.module('happyHour.views.openRoute', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/openRoute/:hash', {
    templateUrl: 'views/open-route/open-route.html',
    controller: 'openRouteController'
  });
}])

.controller('openRouteController', ['$scope', '$routeParams', 'AppStatusPersistenceService', '$location', 'BackendService',
function($scope, $routeParams, AppStatusPersistenceService, $location, BackendService) {
	BackendService.getRoute($routeParams.hash).then(function(route) {
		route = JSON.parse(route[0].data);
		console.log(route);
		AppStatusPersistenceService.setRoute(route);
		$location.path('/currentRoute');
	});
}]);
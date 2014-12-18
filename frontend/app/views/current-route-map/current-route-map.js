angular.module('happyHour.views.currentRouteMap', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/currentRouteMap', {
    templateUrl: 'views/current-route-map/current-route-map.html',
    controller: 'currentRouteMapController'
  });
}])

.controller('currentRouteMapController', ['$scope', function($scope) {
	$scope.test = 'currentRouteMapController!!';
}]);
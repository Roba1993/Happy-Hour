angular.module('happyHour.views.topRoutes', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/topRoutes', {
    templateUrl: 'views/top-routes/top-routes.html',
    controller: 'topRoutesController'
  });
}])

.controller('topRoutesController', ['$scope', function($scope) {
	$scope.test = 'topRoutesController!!';
}]);
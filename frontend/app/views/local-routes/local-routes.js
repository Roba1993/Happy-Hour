angular.module('happyHour.views.localRoutes', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/localRoutes', {
    templateUrl: 'views/local-routes/local-routes.html',
    controller: 'localRoutesController'
  });
}])

.controller('localRoutesController', ['$scope', function($scope) {
	$scope.test = 'localRoutesController!!';
}]);
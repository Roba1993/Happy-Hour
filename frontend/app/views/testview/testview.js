angular.module('happyHour.views.testview', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/testview', {
    templateUrl: 'views/testview/testview.html',
    controller: 'testViewController'
  });
}])

.controller('testViewController', ['$scope', 'RouteGeneratorService', function($scope, RouteGeneratorService) {
	$scope.test = 'testViewController!!';
	RouteGeneratorService.createRoute([], {});
}]);
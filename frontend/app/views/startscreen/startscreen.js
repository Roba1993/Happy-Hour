angular.module('happyHour.views.startScreen', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/startScreen', {
    templateUrl: 'views/startscreen/startscreen.html',
    controller: 'startScreenController'
  });
}])

.controller('startScreenController', ['$scope', 'AppStatusPersistenceService', '$location', function($scope, AppStatusPersistenceService, $location) {
	var route = {
		options: {
			startTime: '18:00',
			endTime: '19:00',
			stayTime: 1,
			radius: 2.5,
			location: {
				longitude: 1,
				latitude: 1
			}
		},
		timeframes: [{
			startTime: '18:00',
			endTime: '19:00',
			bar: {
				'name': 'Lutscher Bar',
				'rating': 1,
				'costs': 5,
				'description': 'Die schlechteste Bar in Stuttgart',
				'imageUrl': '',
				'openingTimes': [
					{
						'startTime': '08:00', 
						'endTime': '20:00', 
						'days': [1,2,3,4,5]
					}, 
					{
						'startTime': '08:00', 
						'endTime': '02:00', 
						'days': [6,7]
					}
				],
				'location': {longitude:9.18293, latitude:48.77585},
				'adress': 'Coole Straße 49 Stuttgart',
				'happyHours': [
					{
						'startTime': '17:00', 
						'endTime': '17:30', 
						'description': 'Pommes 50ct günstiger', 
						'days': [3,7]
					}
				]
			}
		}]
	};

	$scope.buttonClicked = function() {
		AppStatusPersistenceService.setRoute(route);
		$location.path('/currentRoute');
	};
}]);
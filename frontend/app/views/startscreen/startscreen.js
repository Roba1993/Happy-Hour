angular.module('happyHour.views.startScreen', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/startScreen', {
    templateUrl: 'views/startscreen/startscreen.html',
    controller: 'startScreenController'
  });
}])

.controller('startScreenController', ['$scope', 'AppStatusPersistenceService', '$location', 'BackendService', 'RouteGeneratorService',
function($scope, AppStatusPersistenceService, $location, BackendService, RouteGeneratorService) {
	
	// Heutiger Wochentag setzen (JS hat Sonntag intern auf 0, App auf 7)
	var weekdayToday = new Date().getDay();
	if(weekdayToday === 0) {
		weekdayToday = 7;
	}

	// Routenstandardwerte
	var routeOptions = {
		radius: 2.5,
		location: {longitude:9.1833333, latitude: 48.7666667},
		startTime: '18:00',
		endTime: '23:00',
		stayTime: 1,
		weekday: weekdayToday
	};

	// Erstellt die Route
	var createRoute = function() {
		BackendService.getBars(routeOptions.location, routeOptions.radius, routeOptions.weekday).then(function(bars) {
			// Wenn Bars verfügbar sind Route erstellen
			if(bars.length > 0) {
				var route = RouteGeneratorService.createRoute(bars, routeOptions);
				AppStatusPersistenceService.setRoute(route);
				$location.path('/currentRoute');
			}
			// Ansonsten Fehlermeldung anzeigen
			else {
				$scope.noBarPopupOpen = true;
			}
		});
	};

	// Wird aufgerufen, wenn der GoHappy Button geklickt wurde
	$scope.buttonClicked = function() {
		$scope.showLoading = true;
		// Aktuelle Lokation des Gerätes abfragen
		navigator.geolocation.getCurrentPosition(
			// Wenn eine Position zurückgegeben wurde, diese als Routenoption setzen
			function(position) {
				routeOptions.location.latitude = position.coords.latitude;
				routeOptions.location.longitude = position.coords.longitude;
				createRoute();
			},
			// Wenn keine Position zurückgegeben wurde, Route mit Standardoptionen erstellen
			function() {
				createRoute();
			}
		);
	};
}]);
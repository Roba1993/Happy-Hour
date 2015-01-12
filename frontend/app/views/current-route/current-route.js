angular.module('happyHour.views.currentRoute', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/currentRoute', {
    templateUrl: 'views/current-route/current-route.html',
    controller: 'currentRouteController'
  });
}])

.controller('currentRouteController', 
['$scope', 'BackendService', 'RouteGeneratorService', 'RoutesPersistenceService', 'AppStatusPersistenceService', 
function($scope, BackendService, RouteGeneratorService, RoutesPersistenceService, AppStatusPersistenceService) {
	// Aktuellen Pfad persistieren
	AppStatusPersistenceService.setPath('/currentRoute');

	// Aktuelle Route aus dem AppStatus auslesen
	$scope.route = AppStatusPersistenceService.getRoute();

	// TODO: evtl löschen
	if($scope.route === null) {
		$scope.route = {
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
	}

	// Die Route im AppStatus bei jedem Ändern des Routenobjekts aktualisieren
	$scope.$watch('route', function(route) {
		AppStatusPersistenceService.setRoute(route);
		console.log(route);
	}, true);

	// Feuert jedesmal wenn die Sidebar geschlossen wird (auch beim Aufrufen des Controllers)
	$scope.$watch('isSidebarOpen', function(newValue, oldValue) {
		if(!$scope.isSidebarOpen) {
			// Wird nur ausgefürt, wenn sich wirklich etwas geändert hat (nicht beim initialisieren)
			if(newValue !== oldValue) {
				// Timeframes anpassen
				var startTime = time($scope.route.options.startTime);
				var endTime = time($scope.route.options.endTime);
				var newTimeframes = [];

				// Neues Timeframe-Array erstellen
				while(startTime.isBefore(endTime)) {
					newTimeframes.push({
						startTime: startTime.toString(),
						endTime: startTime.add($scope.route.options.stayTime, 'hours').toString(),
						bar: null
					});
				}

				// Neue Timeframes mit alten Timeframes abgleichen und wenn passend übertragen
				_.forEach(newTimeframes, function(timeframe) {
					var newStartTime = time(timeframe.startTime);
					var newEndTime = time(timeframe.endTime);
					_.forEach($scope.route.timeframes, function(oldTimeframe) {
						var oldStartTime = time(oldTimeframe.startTime);
						var oldEndTime = time(oldTimeframe.endTime);

						// Wenn sich die Zeiträume überschneiden Bar übernehmen
						if(
							newStartTime.isBefore(oldStartTime) && newEndTime.isAfter(oldStartTime) ||
							newEndTime.isAfter(oldStartTime) && newStartTime.isBefore(oldEndTime) ||
							newStartTime.isSame(oldStartTime) || newEndTime.isSame(oldEndTime)
						) {
							timeframe.bar = oldTimeframe.bar;
						}
					});
				});

				$scope.route.timeframes = newTimeframes;
			}
		}

		// Alternative Bars für alle Slots abfragen
		$scope.bars = [];
		_.forEach($scope.route.timeframes, function() {
			// TODO Bars korrekt abfragen
			$scope.bars.push([{
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
				},
			    {
					'name': 'Beste Bar',
					'rating': 4,
					'costs': 3,
					'description': 'Die beste Bar in Stuttgart',
					'imageUrl': '',
					'openingTimes': [
						{
							'startTime': '08:00', 
							'endTime': '22:00', 
							'days': [1,2,3,4,5]
						}, 
						{
							'startTime': '08:00', 
							'endTime': '24:00', 
							'days': [6,7]
						}
					],
					'location': {longitude:9.17589, latitude:48.77975},
					'adress': 'Coole Straße 49 Stuttgart',
					'happyHours': [
					{
						'startTime': '20:00', 
						'endTime': '22:00', 
						'description': 'Bier halber Preis', 
						'days': [3,7]
					}
					]
				}
			]);
		});
	});

	$scope.$watch('barChosen', function() {
		// Herausfinden welcher Slot befüllt ist --> passenden Timeframe mit der Bar befüllen
		_.forEach($scope.barChosen, function(bar, index) {
			if(bar !== undefined) {
				$scope.route.timeframes[index].bar = bar;
			}
		});

		// Array leeren um das Erkennen des Indizes jederzeit zu ermöglichen
		$scope.barChosen = [];
	}, true);

	/**
	 * BARS
	 */
	
	// Es können nur die Details einer einzelnen Bar betrachtet werden
	$scope.openFrameIndex = -1;
	$scope.frameClicked = function(index) {
		if(index === $scope.openFrameIndex) {
			$scope.openFrameIndex = -1;
		}
		else {
			$scope.openFrameIndex = index;
		}
	};

	$scope.removeBar = function(index) {
		$scope.route.timeframes[index].bar = null;
	};

	/**
	 * SIDEBAR
	 */
	
	// Die Startposition auf die Geräteposition setzen
	$scope.setLocationToDevice = function() {

	};

	// Die Route auf dem Gerät persistieren
	$scope.saveRoute = function() {
		if($scope.routeName !== '') {
			$scope.route.name = $scope.routeName;
			RoutesPersistenceService.add($scope.route);
			$scope.namePopupOpen = false;
		}
	};

	// start und endTime defaults binden
	if($scope.route.options.startTime !== undefined && $scope.route.options.endTime !== undefined) {
		$scope.routeTime = [$scope.route.options.startTime, $scope.route.options.endTime];
	}
	else {
		// TODO eventuell löschen
		$scope.routeTime = ['20:00', '03:00'];
	}
	// Ausgabe des Sliders in das Options Format umwandeln
	$scope.$watch('routeTime', function(routeTime) {
		$scope.route.options.startTime = routeTime[0];
		$scope.route.options.endTime = routeTime[1];
	});
}]);
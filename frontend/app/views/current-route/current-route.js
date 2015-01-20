angular.module('happyHour.views.currentRoute', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/currentRoute', {
    templateUrl: 'views/current-route/current-route.html',
    controller: 'currentRouteController'
  });
}])

.controller('currentRouteController', 
['$scope', '$location', 'BackendService', 'RouteGeneratorService', 'RoutesPersistenceService', 'AppStatusPersistenceService',
function($scope, $location, BackendService, RouteGeneratorService, RoutesPersistenceService, AppStatusPersistenceService) {
	// Aktuellen Pfad persistieren
	AppStatusPersistenceService.setPath('/currentRoute');

	// Aktuelle Route aus dem AppStatus auslesen
	$scope.route = AppStatusPersistenceService.getRoute();

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
				while(isBeforeOverNight(startTime, endTime)) {
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

			// Alternative Bars für alle Slots abfragen
			$scope.bars = [];
			BackendService.getBars($scope.route.options.location, $scope.route.options.radius, $scope.route.options.weekday).then(function(bars) {
				_.forEach($scope.route.timeframes, function() {
					$scope.bars.push(bars);
				});
			});
		}
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
	 * NAVIGATION
	 */
	$scope.reloadRoute = function() {
		BackendService.getBars($scope.route.options.location, $scope.route.options.radius, $scope.route.options.weekday).then(function(bars) {
			var route = RouteGeneratorService.createRoute(bars, $scope.route.options);
			$scope.route = route;
		});
	};

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

	$scope.reportBar = function(barId, reportBarText) {
		BackendService.reportData(barId, reportBarText);
	};

	/**
	 * SIDEBAR
	 */
	
	// Die Startposition auf die Geräteposition setzen
	$scope.setLocationToDevice = function() {
		navigator.geolocation.getCurrentPosition(function(position) {
			$scope.$apply(function() {
				$scope.route.options.location.longitude = position.coords.longitude;
				$scope.route.options.location.latitude = position.coords.latitude;
			});
		});
	};

	// Die Route auf dem Gerät persistieren
	$scope.saveRoute = function() {
		// Routenname validieren: Darf nicht leer und nicht länger als 50 Zeichen sein
		if($scope.routeName !== '' && $scope.routeName.length <= 50) {
			$scope.route.name = $scope.routeName;
			RoutesPersistenceService.add($scope.route);
			$scope.namePopupOpen = false;
		}
	};

	$scope.shareRoute = function() {
		BackendService.saveRoute($scope.route).then(function(hash) {
			$scope.shareLink = $location.protocol() + '://' + $location.host() + ':' + $location.port() + '/#/openRoute/' + hash;
			$scope.sharePopupOpen = true;
		});
	};

	// startTime und endTime an den Slider binden
	$scope.routeTime = [$scope.route.options.startTime, $scope.route.options.endTime];
	
	// Ausgabe des Sliders in das Options Format umwandeln
	$scope.$watch('routeTime', function(routeTime) {
		$scope.route.options.startTime = routeTime[0];
		$scope.route.options.endTime = routeTime[1];
	});

	/**
	 * HELPER
	 */
	
	/**
     * Bestimmt ob eine Zeit vor der anderen ist, über die Nacht gesehen
     * @param  {Time} time1 Erste Zeit
     * @param  {Time} time2 Zweite Zeit
     * @return {Boolean} `true` wenn time1 < time2
     */
    function isBeforeOverNight(time1, time2) {
		var referenceTime = time('12:00:00');
		if(time1.isAfter(referenceTime) && time2.isBefore(referenceTime)) {
			return !time1.isBefore(time2);
		}
		else {
			return time1.isBefore(time2);
		}
	}
}]);
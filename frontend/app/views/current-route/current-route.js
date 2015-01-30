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
	$scope.$watch(function() {
		// String-Version des Objekts watchen um Deep-Watching zu vermeiden
		return JSON.stringify($scope.route);
	}, function() {
		AppStatusPersistenceService.setRoute($scope.route);
		console.log($scope.route);
	});

	var lastOptions = {};
	var possibleBars = [];
	// Feuert jedesmal wenn die Sidebar geschlossen wird (auch beim Aufrufen des Controllers)
	$scope.$watch('isSidebarOpen', function(newValue, oldValue) {
		// Sidebar wurde geschlossen
		if(!$scope.isSidebarOpen) {
			// Wird nur ausgeführt, wenn sich am Status der Sidebar wirklich etwas verändert hat
			if(newValue !== oldValue) {
				// Wenn sich an der Routenzeit was geändert hat, Timeframes anpassen
				if(
					lastOptions.startTime !== $scope.route.options.startTime ||
					lastOptions.endTime !== $scope.route.options.endTime ||
					lastOptions.stayTime !== $scope.route.options.stayTime
				) {
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

					// Mögliche Bars entsprechend neu setzen
					_.forEach($scope.route.timeframes, function() {
						$scope.bars.push(possibleBars);
					});
				}
			}

			// Wenn sich die Bars betreffende Optionen geändert haben
			if(
				lastOptions.radius !== $scope.route.options.radius ||
				lastOptions.weekday !== $scope.route.options.weekday ||
				lastOptions.location.longitude !== $scope.route.options.location.longitude ||
				lastOptions.location.latitude !== $scope.route.options.location.latitude
			) {
				// Alternative Bars für alle Slots abfragen
				$scope.bars = [];
				$scope.showLoading = true;
				BackendService.getBars($scope.route.options.location, $scope.route.options.radius, $scope.route.options.weekday).then(function(bars) {
					possibleBars = bars;
					// Wenn Bars verfügbar sind, verfügbar machen
					if(possibleBars.length > 0) {
						_.forEach($scope.route.timeframes, function() {
							$scope.bars.push(possibleBars);
						});
					}
					// Ansonsten Fehlermeldung anzeigen
					else {
						$scope.noBarPopupOpen = true;
					}
					$scope.showLoading = false;
				});
			}
		}
		// Sidebar wurde geöffnet
		else {
			lastOptions = _.cloneDeep($scope.route.options);
		}
	});

	/**
	 * NAVIGATION
	 */
	
	// Generiert eine neue Route auf Basis der Routenoptionen
	$scope.reloadRoute = function() {
		$scope.showLoading = true;
		BackendService.getBars($scope.route.options.location, $scope.route.options.radius, $scope.route.options.weekday).then(function(bars) {
			// Wenn Bars verfügbar sind, Route generieren
			if(bars.length > 0) {
				var route = RouteGeneratorService.createRoute(bars, $scope.route.options);
				$scope.route = route;
			}
			// Ansonsten Fehlermeldung anzeigen
			else {
				$scope.noBarPopupOpen = true;
			}
			$scope.showLoading = false;
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

	// Löscht eine Bar aus einem Timeframe
	$scope.removeBar = function(index) {
		$scope.route.timeframes[index].bar = null;
	};

	// Sendet eine Reportnachricht an das Backend
	$scope.reportBar = function(barId, reportBarText) {
		if(reportBarText.length > 0) {
			BackendService.reportData(barId, reportBarText);
			$scope.reportPopupNotification = true;
		}
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
		// Wenn der Speicher voll ist Warnung ausgeben
		if(RoutesPersistenceService.isFull()) {
			$scope.saveErrorPopupOpen = true;
			$scope.namePopupOpen = false;
		}
		else {
			// Routenname validieren: Darf nicht leer und nicht länger als 50 Zeichen sein
			if($scope.routeName !== '' && $scope.routeName.length <= 50) {
				$scope.route.name = $scope.routeName;
				RoutesPersistenceService.add($scope.route);
				$scope.namePopupOpen = false;
			}
		}
	};

	// Sendet eine Route zum sharen an das Backend
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
		var breakTime = time('00:00:00');
		if(time1.isAfter(referenceTime) && time2.isBefore(referenceTime) || time1.isSame(breakTime) && time2.isAfter(referenceTime)) {
			return !time1.isBefore(time2);
		}
		else {
			return time1.isBefore(time2);
		}
	}
}]);
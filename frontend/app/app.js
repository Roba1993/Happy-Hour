// Definiere anwendungsweite Abhängigkeiten
angular.module('happyHour', [
  'ngRoute',
  'ngTouch',
  'happyHour.persistence.RoutesPersistence',
  'happyHour.persistence.AppStatusPersistence',
  'happyHour.backend.Backend',
  'happyHour.algorithm.RouteGenerator',
  'happyHour.map.MapDirective',
  
  'happyHour.filters.DaysFilter',
  'happyHour.filters.TimeFilter',
  'happyHour.directives.RatingDirective',
  'happyHour.directives.PlacesAutocompleteDirective',
  'happyHour.directives.DayPickerDirective',
  'happyHour.directives.SliderDirective',
  'happyHour.directives.BarPickerDirective',

  'happyHour.views.currentRoute',
  'happyHour.views.currentRouteMap',
  'happyHour.views.localRoutes',
  'happyHour.views.topRoutes',
  'happyHour.views.startScreen',
  'happyHour.views.testview'
])
.config(['$routeProvider', 'localStorageServiceProvider', function($routeProvider, localStorageServiceProvider) {
  // Wenn keine passende Route gefunden wird, leite auf den Startscreen weiter
  $routeProvider.otherwise({redirectTo: '/startScreen'});

  // LocalStorage-Prefix setzen
  localStorageServiceProvider.setPrefix('happyHour');
}])
.run(['AppStatusPersistenceService', '$location', function(AppStatusPersistenceService, $location) {
  var path = AppStatusPersistenceService.getPath();
  var route = AppStatusPersistenceService.getRoute();
  var lastTime = AppStatusPersistenceService.getTime();
  var now = new Date().getTime();

  // Öffnungszeit der Anwendung auf JETZT setzen
  AppStatusPersistenceService.setTime(now);

  // Wenn ein Path und eine Route vorhanden sind und nicht 24h seit der letzten Öffnungszeit abgelaufen sind, AppStatus nutzen
  if((path !== null && route !== null) && lastTime + (24*60*60*1000) > now) {
    $location.path(path);
  }
  else {
    // AppStatus löschen
    AppStatusPersistenceService.setPath(null);
    AppStatusPersistenceService.setRoute(null);

    // Auf Startscreen verlinken
    $location.path('/startScreen');
  }
}]);
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

  'happyHour.views.currentRoute',
  'happyHour.views.currentRouteMap',
  'happyHour.views.localRoutes',
  'happyHour.views.topRoutes',
  'happyHour.views.testview'
])
.config(['$routeProvider', 'localStorageServiceProvider', function($routeProvider, localStorageServiceProvider) {
  // Wenn keine passende Route gefunden wird, leite auf currentRoute weiter
  $routeProvider.otherwise({redirectTo: '/currentRoute'});

  // LocalStorage-Prefix setzen
  localStorageServiceProvider.setPrefix('happyHour');
}])
.run(['AppStatusPersistenceService', '$location', function(AppStatusPersistenceService, $location) {
  var path = AppStatusPersistenceService.getPath();
  if(path !== null) {
    $location.path(path);
  }
}]);
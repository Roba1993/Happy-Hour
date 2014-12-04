angular.module('happyHour.persistence.RoutesPersistence', [])
	.factory('RoutesPersistenceService', ['localStorageService', function(localStorageService) {
		return {
			add: function(route) {
				var routes = localStorageService.get('routes');
				routes.push(route);
				localStorageService.set('routes', routes);
			},
			remove: function() {

			},
			get: function() {

			},
			getAll: function() {

			},
			isFull: function() {

			}
		};
	}]);
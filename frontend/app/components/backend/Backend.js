angular.module('happyHour.backend.Backend', [])
	.factory('BackendService', [ function() {
		var service = {
			getBars: function(location, radius, weekday) {
				console.log(location);
				console.log(radius);
				console.log(weekday);
			}
		};

		return service;
	}]);
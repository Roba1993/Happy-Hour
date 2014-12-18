angular.module('happyHour.filters.TimeFilter', [])
	.filter('formatTime', function() {
		return function(input) {
		
			if (input.endTime === null){
				return 'ab ' + input.startTime;
			}
			else {
				return input.startTime + ' - ' + input.endTime;
			}
		};
	});
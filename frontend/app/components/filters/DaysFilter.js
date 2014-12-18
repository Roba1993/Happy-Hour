angular.module('happyHour.filters.DaysFilter', [])
	.filter('formatDays', function() {
		return function(input) {
			var dayNames = [null, 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'];
			var translatedDays = [];
			
			if (input.length === 7){
				return 'Jeden Tag';
			}
			
			_.forEach(input, function(dayNumber) {
				translatedDays.push(dayNames[dayNumber]);
			});
			
			return translatedDays.join('/');
		};
	});
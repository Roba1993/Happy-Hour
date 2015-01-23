/**
 * Tests fuer den Routenalgorithmus
 * @author Markus Thömmes
 */
describe('RouteGenerator', function() {
	beforeEach(function() {
		module('happyHour.algorithm.RouteGenerator');
	});

	it('sollte das Routenobjekt korrekt zusammensetzen', inject(function(RouteGeneratorService) {
		var bars = [
			{
				name: 'besser',
				location: {longitude: 1.0, latitude: 1.0},
				rating: 9
			},
			{
				name: 'schlechter',
				location: {longitude: -1.0, latitude: -1.0},
				rating: 8
			}
		];
		var options = {
			location: {longitude: 0.0, latitude: 0.0},
			stayTime: 1,
			startTime: '10:00',
			endTime: '12:00'
		};

		var route = RouteGeneratorService.createRoute(bars, options);

		expect(route.options).toEqual(options);
		expect(route.timeframes.length).toBe(2);
		expect(route.timeframes[0].startTime).toBe('10:00');
		expect(route.timeframes[0].endTime).toBe('11:00');
		expect(route.timeframes[1].startTime).toBe('11:00');
		expect(route.timeframes[1].endTime).toBe('12:00');
    }));

    it('sollte bei gleicher Entfernung das bessere Rating nehmen', inject(function(RouteGeneratorService) {
		var bars = [
			{
				name: 'besser',
				location: {longitude: 1.0, latitude: 1.0},
				rating: 9
			},
			{
				name: 'schlechter',
				location: {longitude: -1.0, latitude: -1.0},
				rating: 8
			}
		];
		var options = {
			location: {longitude: 0.0, latitude: 0.0},
			stayTime: 1,
			startTime: '10:00',
			endTime: '12:00'
		};

		var route = RouteGeneratorService.createRoute(bars, options);

		expect(route.timeframes[0].bar.name).toBe('besser');
		expect(route.timeframes[1].bar.name).toBe('schlechter');
    }));

    it('sollte bei gleichem Rating die kürzere Entfernung nehmen', inject(function(RouteGeneratorService) {
		var bars = [
			{
				name: 'besser',
				location: {longitude: 1.0, latitude: 1.0},
				rating: 8
			},
			{
				name: 'schlechter',
				location: {longitude: 2.0, latitude: 2.0},
				rating: 8
			}
		];
		var options = {
			location: {longitude: 0.0, latitude: 0.0},
			stayTime: 1,
			startTime: '10:00',
			endTime: '12:00'
		};

		var route = RouteGeneratorService.createRoute(bars, options);

		expect(route.timeframes[0].bar.name).toBe('besser');
		expect(route.timeframes[1].bar.name).toBe('schlechter');
    }));

    it('sollte bei gleichem Rating und Entfernung die Bar mit HappyHour nehmen', inject(function(RouteGeneratorService) {
    	var bars = [
			{
				name: 'besser',
				location: {longitude: 1.0, latitude: 1.0},
				rating: 8,
				happyHours: [
					{
						startTime: '10:00',
						endTime: '11:00',
						days: [3]
					}
				]
			},
			{
				name: 'schlechter',
				location: {longitude: -1.0, latitude: -1.0},
				rating: 10
			}
		];
		var options = {
			location: {longitude: 0.0, latitude: 0.0},
			stayTime: 1,
			startTime: '10:00',
			endTime: '12:00',
			weekday: 3
		};

		var route = RouteGeneratorService.createRoute(bars, options);

		expect(route.timeframes[0].bar.name).toBe('besser');
		expect(route.timeframes[1].bar.name).toBe('schlechter');
    }));

});
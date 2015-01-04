/**
 * Tests für die PlacesAutocompleteDirective
 * @author Markus Thömmes
 */
describe('PlacesAutocompleteDirective', function() {
	var $compile;
	var $rootScope;

	var api;
	var geocoderApi;

	var eventCallback;

	beforeEach(module('happyHour.directives.PlacesAutocompleteDirective'));

	beforeEach(function() {
		module(function ($provide) {
			$provide.value('MapLoaderService', {
				then: function(cb) {
					api = jasmine.createSpyObj('', ['LatLng']);
					api.Geocoder = function() {
						geocoderApi = jasmine.createSpyObj('geocoder', ['geocode']);
						return geocoderApi;
					};
					api.places = {
						Autocomplete: function() {
							return {
								getPlace: function() {
									return {
										geometry: {
											location: {
												lat: function() {
													return 2.0;
												},
												lng: function() {
													return 3.0;
												}
											}
										}
									};
								}
							};
						}
					};

					api.event = {
						addListener: function(api, event, callback) {
							eventCallback = callback;
						}
					};

					spyOn(api.places, 'Autocomplete').and.callThrough();
					spyOn(api.event, 'addListener').and.callThrough();
					cb(api);
				}
			});
		});
	});

	beforeEach(inject(function(_$compile_, _$rootScope_){
		$compile = _$compile_;
		$rootScope = _$rootScope_;
	}));

    it('initialisiert Autocomplete mit dem erzeugten Element', function() {
		var element = $compile('<input-places result="test" />')($rootScope);

		$rootScope.$digest();
		expect(api.places.Autocomplete).toHaveBeenCalled();
		expect(api.places.Autocomplete.calls.argsFor(0)[0]).toBe(element.children()[0]);
    });

    it('bindet das "place_changed" Event', function() {
    	$compile('<input-places result="test" />')($rootScope);

		$rootScope.$digest();
		expect(api.event.addListener).toHaveBeenCalled();
		expect(api.event.addListener.calls.argsFor(0)[1]).toBe('place_changed');
    });

    it('reverse-geokodiert nicht wenn kein "result" gesetzt ist', function() {
    	$compile('<input-places result="test" />')($rootScope);

		$rootScope.$digest();

		expect(geocoderApi.geocode).not.toHaveBeenCalled();
    });

    it('bei einer Aenderung des "result"-Attributs wird es reverse-geokodiert', function() {
    	$rootScope.test = {longitude: 1.0, latitude:2.0};
    	$compile('<input-places result="test" />')($rootScope);

		$rootScope.$digest();

		expect(geocoderApi.geocode).toHaveBeenCalled();

		$rootScope.test = {longitude: 3.0, latitude: 4.0};
		$rootScope.$digest();

		expect(geocoderApi.geocode.calls.count()).toBe(2);
    });

    it('wenn ein Platz ausgewählt wird, wird die "result" Variable mit einem Location-Objekt gefuellt', function() {
    	$compile('<input-places result="test" />')($rootScope);

		$rootScope.$digest();

		expect($rootScope.test).toBeUndefined();

		eventCallback();

		expect($rootScope.test).toEqual({latitude: 2.0, longitude: 3.0});
    });

  });
/**
 * Tests für die MapDirective
 * @author Denis Beck
 */
describe('MapDirective', function() {
  var $compile;
  var $rootScope;

  var api;
  var geocoderApi;

  var eventCallback;

  beforeEach(module('happyHour.map.MapDirective'));

  beforeEach(function() {
    module(function ($provide) {
      $provide.value('MapLoaderService', {
        then: function(cb) {
          api = jasmine.createSpyObj('', ['LatLng', 'Map']);

          api.Geocoder = function() {
            geocoderApi = jasmine.createSpyObj('geocoder', ['geocode']);
            return geocoderApi;
          };

          api.MapTypeId = {};
          api.MapTypeId.ROADMAP = 'roadmap';

          api.event = {
            addListener: function(api, event, callback) {
              eventCallback = callback;
            }
          };

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

  it('erstellen einer Map mit default Optionen', function() {
    var element = $compile('<map />')($rootScope);

    $rootScope.$digest();
    expect(api.Map).toHaveBeenCalled();
    expect(api.Map.calls.argsFor(0)[0]).toBe(element.children()[0]);
  });

  /*it('erstellen einer Map mit Marker', function() {
    var bar1 = {
      id: 'd362985702875md5',
      name: 'Beste Bar',
      rating: 4,
      costs: 3,
      description: 'Die beste Bar in Stuttgart',
      imageUrl: 'http://www.pic.image.png',
      openingTimes: [
        {
          startTime: '08:00',
          endTime: '10:00',
          days: [1,2,3,4,5]
        },
        {
          startTime: '08:00',
          endTime: '12:00',
          days: [6,7]
        }
      ],
      location: {
        'latitude': 35.7348,
        'longitude': 49.0133
      },
      address: 'Coole Straße 49 Stuttgart',
      happyHours: [{
        'startTime': '09:00',
        'endTime': '11:00',
        'description': 'Nice 2 Happy-Hour',
        'days': [3,7]
      }
      ]
    };
    $rootScope.markerArray = {};
    $rootScope.markerArray.push(bar1);
    $compile('<map markers="markerArray" />')($rootScope);

    $rootScope.$digest();
    expect(api.event.addListener).toHaveBeenCalled();
    expect(api.event.addListener.calls.argsFor(0)[1]).toBe('place_changed');
  });*/
});
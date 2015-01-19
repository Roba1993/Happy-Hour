/**
 * Tests für die MapDirective
 * @author Denis Beck
 */
describe('MapDirective', function () {
  var $compile;
  var $rootScope;

  var api;
  //var geocoderApi;
  var directionsRendererApi;
  var directionsServiceApi;

  var eventCallback;

  beforeEach(module('happyHour.map.MapDirective'));

  beforeEach(function () {
    module(function ($provide) {
      $provide.value('MapLoaderService', {
        then: function (cb) {
          api = jasmine.createSpyObj('', ['LatLng', 'Map', 'Marker', 'DirectionsService']);

          api.DirectionsRenderer = function () {
            directionsRendererApi = jasmine.createSpyObj('directionsDisplay', ['setMap', 'setDirections']);
            return directionsRendererApi;
          };

          api.DirectionsService = function () {
            directionsServiceApi = jasmine.createSpyObj('directionsService', ['route']);
            return directionsServiceApi;
          };

          api.DirectionsTravelMode = {};
          api.DirectionsTravelMode.WALKING = 'walking';

          /*api.Geocoder = function () {
            geocoderApi = jasmine.createSpyObj('geocoder', ['geocode']);
            return geocoderApi;
          };*/

          api.MapTypeId = {};
          api.MapTypeId.ROADMAP = 'roadmap';

          api.event = {
            addListener: function (api, event, callback) {
              eventCallback = callback;
            }
          };

          spyOn(api.event, 'addListener').and.callThrough();
          cb(api);
        }
      });
    });
  });

  beforeEach(inject(function (_$compile_, _$rootScope_) {
    $compile = _$compile_;
    $rootScope = _$rootScope_;
  }));

  it('erstellen einer Map mit default Optionen', function () {
    $rootScope.defaultMapOptions = {};
    $rootScope.defaultMapOptions = {
      mapTypeId: 'roadmap',
      mapTypeControl: false,
      streetViewControl: false,
      center: {},
      zoom: 14,
      draggable: true,
      panControl: true,
      rotateControl: true,
      scaleControl: true,
      scrollwheel: true,
      zoomControl: true
    };
    var element = $compile('<map />')($rootScope);

    $rootScope.$digest();

    expect(api.LatLng).toHaveBeenCalled();
    expect(api.LatLng.calls.argsFor(0)[0]).toBe(48.775556);
    expect(api.LatLng.calls.argsFor(0)[1]).toBe(9.182778);

    expect(api.Map).toHaveBeenCalled();
    expect(api.Map.calls.argsFor(0)[0]).toBe(element.children()[0]);
    //expect(api.Map.calls.argsFor(0)[1]).toBe($rootScope.defaultMapOptions);
  });

  it('erstellen einer Map mit Marker', function () {
    $rootScope.markerArray = [
      {
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
            days: [1, 2, 3, 4, 5]
          },
          {
            startTime: '08:00',
            endTime: '12:00',
            days: [6, 7]
          }
        ],
        location: {
          latitude: 35.7348,
          longitude: 49.0133
        },
        address: 'Coole Straße 49 Stuttgart',
        happyHours: [{
          startTime: '09:00',
          endTime: '11:00',
          description: 'Nice 2 Happy-Hour',
          days: [3, 7]
        }
        ]
      }
    ];
    $compile('<map markers="markerArray" />')($rootScope);

    $rootScope.$digest();

    expect(api.Marker).toHaveBeenCalled();
    //expect(api.Marker.calls.argsFor(0)[1]).toBe();
  });

  it('erstellen einer Map mit Route', function () {
    $rootScope.testRoute = {
      id: 1,
      link: 'e98723958987325md5',
      name: 'Top Route',
      options: {
        location: {
          latitude: 35.7348,
          longitude: 49.0133
        },
        radius: 2.5,
        stayTime: 60,
        weekday: 6,
        startTime: '08:00',
        endTime: '12:00'
      },
      timeframes: [{
        startTime: '08:00',
        endTime: '09:00',
        bar: {
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
              days: [1, 2, 3, 4, 5]
            },
            {
              startTime: '08:00',
              endTime: '12:00',
              days: [6, 7]
            }
          ],
          location: {
            latitude: 35.7348,
            longitude: 49.0133
          },
          address: 'Coole Straße 49 Stuttgart',
          happyHours: [{
            startTime: '09:00',
            endTime: '11:00',
            description: 'Nice 2 Happy-Hour',
            days: [3, 7]
          }
          ]
        }
      },
        {
          startTime: '10:00',
          endTime: '11:00',
          bar: {
            id: 'd362985702875md5',
            name: 'Schlechteste Bar',
            rating: 1,
            costs: 5,
            description: 'Die schlechteste Bar in Stuttgart',
            imageUrl: 'http://www.pic.image.png',
            openingTimes: [
              {
                startTime: '08:00',
                endTime: '10:00',
                days: [1, 2, 3, 4, 5]
              },
              {
                startTime: '08:00',
                endTime: '12:00',
                days: [6, 7]
              }
            ],
            location: {
              latitude: 35.7348,
              longitude: 49.0133
            },
            address: 'Schlechte Straße 49 Stuttgart',
            happyHours: [{
              startTime: '09:00',
              endTime: '11:00',
              description: 'Nice 2 Happy-Hour',
              days: [3, 7]
            }
            ]
          }
        }
      ]
    };
    $compile('<map route="testRoute" />')($rootScope);

    $rootScope.$digest();

    expect(directionsRendererApi.setMap).toHaveBeenCalled();
    expect(directionsServiceApi.route).toHaveBeenCalled();
  });
});
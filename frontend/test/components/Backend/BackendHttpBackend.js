/**
 * httpBackend wird genutzt um die REST Schnittstellen des Backends zu simulieren.
 *
 * @author Kim Rinderknecht, Daniel Reichert
 */

describe('der BackendService', function(){
    beforeEach(function() {
       module('happyHour.backend.Backend'); 
    });
    
    it('should call $http.get and deliver JSON Data', inject(function (BackendService, $httpBackend) {
    $httpBackend.expectGET('http://localhost:8080/bars?lat=35.7348&long=49.0133&radius=2.5&weekday=1').respond([
        {'name': 'ergebnisObjekt', 
        'description': 'sdvisiusdkjbgksjbkSDVI o gsi', 
        'timestamp': '2014-12-02 15:00:00', 
        'status': 'success ', 
        'data': [
            {'id': 'd362985702875md5',
            'name': 'Beste Bar',
            'rating': 4,
            'costs': 3,
            'description': 'Die beste Bar in Stuttgart',
            'imageUrl': 'http://www.pic.image.png',
            'openingTimes': [
            {
                'startTime': '08:00', 
                'endTime': '10:00', 
                'days': [1,2,3,4,5]
            }, 
            {
                'startTime': '08:00', 
                'endTime': '12:00', 
                'days': [6,7]
            }
            ],
            'adress': 'Coole Straße 49 Stuttgart',
            },
            {'id': 'd362985702875md5',
            'name': 'Beste Bar',
            'rating': 4,
            'costs': 3,
            'description': 'Die beste Bar in Stuttgart',
            'imageUrl': 'http://www.pic.image.png',
            'openingTimes': [
            {
                'startTime': '08:00', 
                'endTime': '10:00', 
                'days': [1,2,3,4,5]
            }, 
            {
                'startTime': '08:00', 
                'endTime': '12:00', 
                'days': [6,7]
            }
            ],
            'adress': 'Coole Straße 49 Stuttgart'
            }
          ]}
    ]);
    
    BackendService.getBars( 
            {'name': 'ergebnisObjekt', 
            'description': 'sdvisiusdkjbgksjbkSDVI o gsi', 
            'timestamp': '2014-12-02 15:00:00', 
            'status': 'success ', 
            'data': {'latitude': 35.7348, 'longitude': 49.0133}}, 2.5, 1);
    
    $httpBackend.flush();
    }));

    it('should call $http.get and deliver JSON Data when calling getRoute', inject(function (BackendService, $httpBackend) {
    $httpBackend.expectGET('http://localhost:8080/routes/dk390nv303ijrv').respond([
        {'name': 'RouteStgt', 
         'description': 'Super Route in Stuttgart', 
         'timestamp': '2014-12-02 15:00:00', 
         'status': 'success', 
         'data':
        {'id': 1,
         'link': 'e98723958987325md5',
         'name': 'Top Route'
        }
        }
    ]);
    
    BackendService.getRoute('dk390nv303ijrv');
    $httpBackend.flush();
    }));
});



  
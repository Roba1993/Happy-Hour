/**
 * httpBackend wird genutzt um die REST Schnittstellen des Backends zu simulieren.
 *
 * @author Kim Rinderknecht, Daniel Reichert
 */

describe('der BackendService', function(){
    beforeEach(function() {
       module('happyHour.backend.Backend'); 
    });

// Test für getBars Service
    it('should call $http.get and deliver JSON Data when calling getBars', inject(function (BackendService, $httpBackend) {
    $httpBackend.expectGET('http://localhost:8080/bars?lat=35.7348&long=49.0133&radius=2.5&weekday=1').respond([
        // Beispielhaftes verkleinertes JSON Objekt, das NICHT zu zukünftigen Objekt entspricht, aber zu Testzwecken ausreicht.
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
    
// Hier wird der Service getBars aufgerufen und somit der Test gestartet. Ein Locationobjekt sowie der Radius und Wochentag wird übergeben. Wenn der Service korrekt geschrieben wurde, wird die REST Abfrage mit der oben geschriebenen Funktion abgefangen und das JSON Objekt, welches nach respond steht, zurück geliefert.
    BackendService.getBars( 
            {'name': 'ergebnisObjekt', 
            'description': 'sdvisiusdkjbgksjbkSDVI o gsi', 
            'timestamp': '2014-12-02 15:00:00', 
            'status': 'success ', 
            'data': {'latitude': 35.7348, 'longitude': 49.0133}}, 2.5, 1);
    
    $httpBackend.flush();
    }));

    
// Test für getRoute Service
    it('should call $http.get and deliver JSON Data when calling getRoute', inject(function (BackendService, $httpBackend) {
    $httpBackend.expectGET('http://localhost:8080/routes/dk390nv303ijrv').respond([
        // Beispielhaftes verkleinertes JSON Objekt, das NICHT zu zukünftigen Objekt entspricht, aber zu Testzwecken ausreicht.
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

// Hier wird der Service getRoute aufgerufen und somit der Test gestartet. Ein Hashwert einer gespeicherten Route wird übergeben. Wenn der Service korrekt geschrieben wurde, wird die REST Abfrage mit der oben geschriebenen Funktion abgefangen und das JSON Objekt, welches nach respond steht, zurück geliefert.
    BackendService.getRoute('dk390nv303ijrv');
    $httpBackend.flush();
    }));
});



  
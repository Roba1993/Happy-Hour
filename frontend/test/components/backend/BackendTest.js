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
    $httpBackend.expectGET('http://localhost:8080/bars?latitude=35.7348&longitude=49.0133&radius=2.5&weekday=1').respond([
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
    BackendService.getBars({'latitude': 35.7348, 'longitude': 49.0133}, 2.5, 1);
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
    
// Test für getReports Service
    it('should call $http.get and deliver JSON Data when calling getReports', inject(function (BackendService, $httpBackend) {
    $httpBackend.expectGET('http://localhost:8080/bars/reports').respond([
        // Beispielhaftes JSON Objekt, das zukünftigen Objekt entspricht, und zu Testzwecken ausreicht.
        {'name': 'Reported Bars',
         'description': 'Alle gemeldeten Bars',
         'timestamp': '2014-12-02 15:00:00',
         'status': 'success',
         'data': [
        {'id': '19237f4f8200',
         'description': 'Happy Hour falsch'
        },
        {'id': '28736482rxh9x8',
         'description': 'Öffnungszeite'
        },
        {'id': '82379rx8923jx8',
         'description': 'Top Route'
        }
        ]}
    ]);

// Hier wird der Service getReports aufgerufen und somit der Test gestartet. Wenn der Service korrekt geschrieben wurde, wird die REST Abfrage mit der oben geschriebenen Funktion abgefangen und das JSON Objekt, welches nach respond steht, zurück geliefert.
    BackendService.getReports();
    $httpBackend.flush();
    }));
    
    

// Test für reportData Service
it('should call $http.post and save JSON Data when calling reportData', inject(function (BackendService, $httpBackend) {
  $httpBackend.expectPOST('http://localhost:8080/bars/1241/reports/').respond([
    // Beispielhaftes verkleinertes JSON Objekt, das NICHT zu zukünftigen Objekt entspricht, aber zu Testzwecken ausreicht.
    {
      'name': 'Daten melden',
      'description': 'Fehlerhafte Daten einer Bar melden',
      'timestamp': '2014-12-02 15:00:00',
      'status': 'success',
      'data': 'erfolgreich gemeldet'
    }
  ]);

// Hier wird der Service reportData aufgerufen und somit der Test gestartet. Ein Boolean Wert wird zurückgegeben, ob das Melden erfolgreich war oder nicht.
//Wenn der Service korrekt geschrieben wurde, wird die REST Abfrage mit der oben geschriebenen Funktion abgefangen und das JSON Objekt, welches nach respond steht, zurück geliefert.
  BackendService.reportData(1241, 'Uhrzeit für die Happy Hour falsch'
  );
  $httpBackend.flush();
}));

    
// Test für saveHappy Service
it('should call $http.post and save JSON Data when calling saveHappy', inject(function (BackendService, $httpBackend) {
  $httpBackend.expectPOST('http://localhost:8080/123456789/hour').respond([true]);

// Hier wird der Service saveHappy aufgerufen und somit der Test gestartet. Ein Boolean Wert wird zurückgegeben, ob das Speichern erfolgreich war oder nicht.
//Wenn der Service korrekt geschrieben wurde, wird die REST Abfrage mit der oben geschriebenen Funktion abgefangen und einen Boolean zurück geben.
  BackendService.saveHappy(
      {
        'name': 'HappyHour',
        'description': 'Spiegelt eine HappyHour wider',
        'timestamp': '2014-12-02 15:00:00',
        'status': 'success',
        'data': {
            'startTime': '09:00', 
            'endTime': '11:00', 
            'description': 'Nice 2 Happy-Hour', 
            'days': [3,7]
            }
        }, 123456789);
  $httpBackend.flush();
}));

});

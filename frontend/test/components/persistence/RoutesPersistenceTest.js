/**
 * Tests für das RoutesPersistence Objekt
 * @author Dorothee Nies
 */
describe('happyHour.persistence.RoutesPersistence module', function() {
  var store = {};
  beforeEach(function() {
    store = {};

    module('happyHour.persistence.RoutesPersistence');
    module(function ($provide) {
      $provide.value('localStorageService', {
        set: function(key, value) {
          store[key] = value;
        },
        get: function(key) {
          if(store[key] === undefined) {
            return null;
          }
          return store[key];
        }
      });
    });
  });

  describe('RoutesPersistenceService', function(){

    it('sollte Routen hinzufügen', inject(function(RoutesPersistenceService) {
      expect(RoutesPersistenceService).toBeDefined();

      RoutesPersistenceService.add({id:1});
      expect(store.routes.length).toBe(1);

      RoutesPersistenceService.add({id:2});
      expect(store.routes.length).toBe(2);
    }));

    it('sollte keine Routen zu voller Liste hinzufügen', inject(function(RoutesPersistenceService) {
      expect(RoutesPersistenceService).toBeDefined();

      RoutesPersistenceService.add({id:1});
      RoutesPersistenceService.add({id:1});
      RoutesPersistenceService.add({id:1});
      RoutesPersistenceService.add({id:1});
      RoutesPersistenceService.add({id:1});
      RoutesPersistenceService.add({id:1});
      RoutesPersistenceService.add({id:1});
      RoutesPersistenceService.add({id:1});
      RoutesPersistenceService.add({id:1});
      RoutesPersistenceService.add({id:1});


      expect(store.routes.length).toBe(10);

      expect(RoutesPersistenceService.add({id:2})).toBe(false);
      expect(store.routes.length).toBe(10);
    }));

    it('sollte den Timestamp beim speichern mitspeichern', inject(function(RoutesPersistenceService) {
    	var id = RoutesPersistenceService.add({id:1});
    	expect(RoutesPersistenceService.get(id).timestamp instanceof Date).toBe(true);
    }));

    it('sollte zweimal die selbe Route mit unterschiedlichen Hashes speichern', function(done) {
      inject(function(RoutesPersistenceService) {
      	var id = RoutesPersistenceService.add({id:1});

        window.setTimeout(function() {
          var id2 = RoutesPersistenceService.add({id:1});

          expect(id).not.toBe(id2);
          done();
        }, 1);
      });
    });
	
	it('sollte Routen löschen', inject(function(RoutesPersistenceService) {
      expect(RoutesPersistenceService).toBeDefined();

      var id = RoutesPersistenceService.add({id:1});
	  RoutesPersistenceService.add({id:2});
	  RoutesPersistenceService.remove(id);
      expect(store.routes.length).toBe(1);
	  
	}));
	  
	it('sollte nicht aus leerer Liste löschen', inject(function(RoutesPersistenceService) {
      expect(RoutesPersistenceService).toBeDefined();
	  
	  var test1 = RoutesPersistenceService.remove(1);
	  expect(test1).toBe(false);
	}));
	  
	it('sollte keine nicht vorhandenen Routen löschen', inject(function(RoutesPersistenceService) {
	  RoutesPersistenceService.add({id:1});
	  RoutesPersistenceService.add({id:2});
	  expect(store .routes.length).toBe(2);
	  
	  var test2 = RoutesPersistenceService.remove(3);
	  expect(test2).toBe(false);
	  expect(store .routes.length).toBe(2);
	  
	  
    }));
	
	it('sollte Routen zurückgeben', inject(function(RoutesPersistenceService) {
      expect(RoutesPersistenceService).toBeDefined();

      var id = RoutesPersistenceService.add({id:1});
	  var test1 = RoutesPersistenceService.get(id);
      expect(test1.id).toBe(id);

    }));
	
	it('sollte keine nicht vorhandenen Routen zurückgeben', inject(function(RoutesPersistenceService) {
      expect(RoutesPersistenceService).toBeDefined();

      RoutesPersistenceService.add({id:1});
	  var test1 = RoutesPersistenceService.get(3);
      expect(test1).toBe(null);

    }));
	
	it('sollte alle vorhandenen Routen zurückgeben', inject(function(RoutesPersistenceService) {
      expect(RoutesPersistenceService).toBeDefined();

      var id1 = RoutesPersistenceService.add({id:1});
	  var id2 = RoutesPersistenceService.add({id:3});
	  var id3 = RoutesPersistenceService.add({id:8});
	  var id4 = RoutesPersistenceService.add({id:7});
	  var id5 = RoutesPersistenceService.add({id:2});
	  var test1 = RoutesPersistenceService.getAll();
	  var ids = [id1, id2, id3, id4, id5];
	  for(var i = 0; i < test1.length; i++) {
		expect(test1[i].id).toBe(ids[i]);
	  }

    }));
	
	it('sollte ein leeres Array zurückgeben wenn keine Routen verfügbar sind', inject(function(RoutesPersistenceService) {
      expect(RoutesPersistenceService).toBeDefined();

      var test1 = RoutesPersistenceService.getAll();
	  expect(test1.length).toBe(0);

    }));
	
	it('sollte true zurückgeben wenn die Liste voll ist', inject(function(RoutesPersistenceService) {
      expect(RoutesPersistenceService).toBeDefined();
	
	  RoutesPersistenceService.add({id:1});
	  RoutesPersistenceService.add({id:3});
	  RoutesPersistenceService.add({id:8});
	  RoutesPersistenceService.add({id:7});
	  RoutesPersistenceService.add({id:2});
	  RoutesPersistenceService.add({id:1});
	  RoutesPersistenceService.add({id:3});
	  RoutesPersistenceService.add({id:8});
	  RoutesPersistenceService.add({id:7});
	  RoutesPersistenceService.add({id:2});
	  
	  var test1 = RoutesPersistenceService.isFull();
	  
	  expect(test1).toBe(true);

    }));
	
	it('sollte false zurückgeben wenn die Liste nicht voll ist', inject(function(RoutesPersistenceService) {
      expect(RoutesPersistenceService).toBeDefined();
	
	  RoutesPersistenceService.add({id:1});
	  RoutesPersistenceService.add({id:3});

	  
	  var test1 = RoutesPersistenceService.isFull();
	  
	  expect(test1).toBe(false);

    }));

  });
});
/**
 * Tests fuer das AppStatusPersistence Objekt
 * @author Dorothee Nies
 */
describe('happyHour.persistence.AppStatusPersistence module', function() {
  var store = {};
  beforeEach(function() {
    store = {};

    module('happyHour.persistence.AppStatusPersistence');
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

  describe('AppStatusPersistence', function(){

    it('sollte den Status speichern', inject(function(AppStatusPersistenceService) {
      expect(AppStatusPersistenceService).toBeDefined();
		
      AppStatusPersistenceService.setStatus(3);

      expect(store.appStatus).toBe(3);

    }));
	
	 it('sollte den Status der Anwendung zurueckgeben', inject(function(AppStatusPersistenceService) {
      expect(AppStatusPersistenceService).toBeDefined();
		
      AppStatusPersistenceService.setStatus(5);
	  var test = AppStatusPersistenceService.getStatus();
	  
	  expect(test).toBe(5);

    }));
	
	

  });
});
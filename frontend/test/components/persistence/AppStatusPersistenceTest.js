/**
 * Tests fuer das AppStatusPersistence Objekt
 * @author Markus Thömmes
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


    it('sollte eine Route speichern und wieder zurückgeben', inject(function(AppStatusPersistenceService) {
      expect(AppStatusPersistenceService).toBeDefined();
		
      AppStatusPersistenceService.setRoute(3);

      expect(store.appStatus.currentRoute).toBe(3);

      expect(AppStatusPersistenceService.getRoute()).toBe(3);

    }));
	
	 it('sollte einen Pfad speichern und wieder zurückgeben', inject(function(AppStatusPersistenceService) {
      expect(AppStatusPersistenceService).toBeDefined();
		
      AppStatusPersistenceService.setPath('test');
      expect(store.appStatus.appPath).toBe('test');
      expect(AppStatusPersistenceService.getPath()).toBe('test');

    }));
	
	

});
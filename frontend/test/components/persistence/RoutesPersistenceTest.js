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

  });
});
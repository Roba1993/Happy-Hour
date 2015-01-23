module.exports = function(config){
  config.set({

    basePath : './',

    files : [
      'app/bower_components/jquery/dist/jquery.min.js',
      'app/bower_components/time-js/time.js',
      'app/bower_components/ionrangeslider/js/ion.rangeSlider.min.js',
      'app/bower_components/angular/angular.js',
      'app/bower_components/angular-route/angular-route.js',
      'app/bower_components/angular-mocks/angular-mocks.js',
      'app/bower_components/angular-local-storage/dist/angular-local-storage.min.js',
      'app/bower_components/lodash/dist/lodash.js',
      'app/bower_components/cryptojslib/rollups/md5.js',
      'app/view*/**/*.js',
      'app/components/**/*.js',
      'app/components/**/*.html',
      'test/**/*.js'
    ],

    exclude: [
      'app/components/algorithm/RouteGenerator_Felix.js'
    ],

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['PhantomJS'],

    plugins : [
      'karma-chrome-launcher',
      'karma-firefox-launcher',
      'karma-phantomjs-launcher',
      'karma-jasmine',
      'karma-ng-html2js-preprocessor'
    ],

    preprocessors: {
      'app/components/**/*.html': ['ng-html2js']
    },

    ngHtml2JsPreprocessor: {
      stripPrefix: 'app/',
      moduleName: 'happyHour.templates'
    }

  });
};
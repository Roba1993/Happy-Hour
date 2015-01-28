module.exports = function(grunt) {
	grunt.initConfig({
		jshint: {
			options: {
				jshintrc: '.jshintrc',
			},
			all: ['app/components/**/*.js', 'app/views/**/*.js', 'test/**/*.js', '!app/bower_components/**/*.js']
		},

		karma: {
			unit: {
				configFile: 'karma.conf.js',
				singleRun: true
			}
		},

		processhtml: {
			dist: {
				files: {
					'dist/index.html': ['app/index.html']
				}
			}
		},

		copy: {
			dist: {
				files: [
					//{expand: true, cwd: 'app/', src: ['images/**'], dest: 'dist/'},
					{expand: true, cwd: 'app/', src: ['fonts/**'], dest: 'dist/'},
					{expand: true, cwd: 'app/', src: ['*.ico'], dest: 'dist/'},
					{expand: true, cwd: 'app/', src: ['views/**/*.html'], dest: 'dist/'},
					{expand: true, cwd: 'app/', src: ['components/**/*.html'], dest: 'dist/'},
				]
			},
		},

		imagemin: {
			dist: {
				files: [
					{
						expand: true,
						cwd: 'app/',
						src: '**/*.{png,jpg,gif,svg}',
						dest: 'dist/'
					}
				]
			}
		},

		uglify: {
			dist: {
				files: {
					'dist/app.min.js': [
						'app/components/persistence/RoutesPersistence.js',
						'app/components/persistence/AppStatusPersistence.js',
						'app/components/backend/Backend.js',
						'app/components/algorithm/RouteGenerator.js',
						'app/components/map/MapLoader.js',
						'app/components/map/MapDirective.js',
						'app/components/filters/DaysFilter.js',
						'app/components/filters/TimeFilter.js',
						'app/components/directives/RatingDirective.js',
						'app/components/directives/PlacesAutocompleteDirective.js',
						'app/components/directives/DayPickerDirective.js',
						'app/components/directives/SliderDirective.js',
						'app/components/directives/BarPickerDirective.js',
						'app/app.js',
						'app/views/current-route/current-route.js',
						'app/views/current-route-map/current-route-map.js',
						'app/views/local-routes/local-routes.js',
						'app/views/top-routes/top-routes.js',
						'app/views/startscreen/startscreen.js',
						'app/views/open-route/open-route.js'
					]
				}
			}
		},

		concat: {
			dist: {
				src: [
					'app/bower_components/jquery/dist/jquery.min.js', 
					'app/bower_components/ionrangeslider/js/ion.rangeSlider.min.js', 
					'app/bower_components/lodash/dist/lodash.min.js',
					'app/bower_components/cryptojslib/rollups/md5.js',
					'app/bower_components/time-js/time.js',
					'app/bower_components/angular/angular.min.js',
					'app/bower_components/angular-route/angular-route.min.js',
					'app/bower_components/angular-touch/angular-touch.min.js',
					'app/bower_components/angular-local-storage/dist/angular-local-storage.min.js',
					'dist/app.min.js'
				],
				dest: 'dist/app.min.js',
			},
		},

		cssmin: {
			target: {
				files: {
					'dist/css/style.min.css': [
						'app/css/fonts.css', 
						'app/css/normalize.css',
						'app/css/grid.css',
						'app/css/helper.css',
						'app/css/elements.css',
						'app/bower_components/ionrangeslider/css/ion.rangeSlider.css',
						'app/bower_components/ionrangeslider/css/ion.rangeSlider.skinHTML5.css',
						'app/css/style.css',
						'app/css/frontend.css'
					]
				}
			}
		}
	});

	grunt.loadNpmTasks('grunt-karma');
	grunt.loadNpmTasks('grunt-contrib-jshint');
	grunt.loadNpmTasks('grunt-processhtml');
	grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-cssmin');
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-imagemin');
	grunt.loadNpmTasks('grunt-newer');

	grunt.registerTask('default', ['jshint', 'karma', 'processhtml', 'uglify', 'concat', 'cssmin', 'newer:imagemin:dist', 'copy']);
	grunt.registerTask('test', ['karma']);
	grunt.registerTask('build', ['processhtml', 'uglify', 'concat', 'cssmin', 'newer:imagemin:dist', 'copy']);
};
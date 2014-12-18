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
		}
	});

	grunt.loadNpmTasks('grunt-karma');
	grunt.loadNpmTasks('grunt-contrib-jshint');

	grunt.registerTask('default', ['jshint', 'karma']);
	grunt.registerTask('test', ['karma']);
};
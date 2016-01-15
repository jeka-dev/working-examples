module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    concat: {
    	dist: {
    		src: [
    		      'src/js/*.js', // tous les JS dans libs
    		      'libs/*.js'  // ce fichier là
    	    ],
    	    dest: 'build/all.js'
    	}
    }
  });
  
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.registerTask('default', ['concat']);

};
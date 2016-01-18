module.exports = function(grunt) {

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    concat: {
    	dist: {
    		src: [
    		      'src/js/*.js',
    		      'libs/*.js'
    	    ],
    	    dest: 'build/all.js'
    	}
    },
    copy: {
    	  main: {
    	    files: [
    	      {expand: false, src: ['src/style/*'], dest: 'build/', filter: 'isFile'}
    	    ],
    	  },
    	},
  });
  
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.registerTask('default', ['concat', 'copy']);

};
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
    	    dest: 'build/prod/all.js'
    	}
    },
    copy: {
    	  main: {
    	    files: [
    	      {expand: false, src: 'src/style/main.css', dest: 'build/prod/main.css', flatten:true},
    	      {expand: false, src: 'src/html/main.html', dest: 'build/prod/main.html', flatten:true}
    	    ],
    	  },
    	},
  });
  
  grunt.loadNpmTasks('grunt-contrib-copy');
  grunt.loadNpmTasks('grunt-contrib-concat');
  grunt.registerTask('default', ['concat', 'copy']);

};
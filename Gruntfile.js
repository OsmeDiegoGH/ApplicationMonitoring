module.exports = function (grunt) {
    grunt.initConfig({
        webAppDir: 'src/main/webapp',
        pkg: grunt.file.readJSON('package.json'),
        watch: {
            options: {
                debounceDelay: 500,
                reload: true,
                atBegin: true
            },
            dev: {
                files: ['<%=webAppDir%>/**/*.js', '<%=webAppDir%>/**/*.less', '<%=webAppDir%>/**/*.html', '!<%=webAppDir%>/resources/**'],
                tasks: ['dev']
            }
        },
        jshint: {
            prod: {
                options: {devel: false}, src: ['<%=webAppDir%>/resources/**/*.js', '!<%=webAppDir%>/resources/vendor/**', '!<%=webAppDir%>/resources/images/**']
            },
            dev: {
                options: {devel: true}, src: ['<%=webAppDir%>/resources/**/*.js', '!<%=webAppDir%>/resources/vendor/**']
            }
        },
        clean: {
            tmp: ['<%=webAppDir%>/resources/tmp/'],
            resources: ['<%=webAppDir%>/resources/modules/', '<%=webAppDir%>/resources/vendor/', '<%=webAppDir%>/resources/libs/']
        },
        copy: {
            tmp_cache: {
                expand: true,
                cwd: '<%=webAppDir%>/',
                src: ['**', '!resources/**', '!WEB-INF/**'],
                dest: '<%=webAppDir%>/resources/tmp/cache/'
            },
            fake_js_min: {
                options: {
                    process: function (src, filepath) {
                        return '"use strict";\n(function(){\n\n' + src + '\n\n})();';
                    }
                },
                expand: true,
                cwd: '<%=webAppDir%>/resources/tmp/',
                src: ['modules/js/**', 'libs/js/**'],
                dest: '<%=webAppDir%>/resources/',
                rename: function(dest, src){
                    if(src.length > 0 && src.endsWith(".js")){
                        src = src.replace(/\.js$/, ".min.js");
                    }
                    return dest + '/' + src; 
                }
            }            
        },
        ngAnnotate: {
            tmp: {
                options: { add: true },
                files: [
                    {
                        expand: true,
                        src: ['!<%=webAppDir%>/resources/tmp/cache/**', '<%=webAppDir%>/resources/tmp/**/*.js'],
                        ext: '', 
                        extDot: ''
                    }
                ]
            }
        },
        removelogging: {
            resources: { src: ['<%=webAppDir%>/resources/**/*.js', '!<%=webAppDir%>/resources/vendor/*.js'] }
        },
        uglify: {
            options: {
                banner: '"use strict";\n(function(){\n\n',
                footer: '\n\n})();'
            },
            resources: {
                files: [{
                        expand: true,
                        cwd: '<%=webAppDir%>/resources/tmp/',
                        src: ['modules/**/*.js', 'libs/**/*.js'],
                        dest: '<%=webAppDir%>/resources/',
                        ext: '.min.js'
                    }]
            }
        },
        less: {
            resources:{
                files: [{
                    expand: true,
                    cwd: '<%=webAppDir%>/resources/tmp/',
                    src: ['modules/css/*.less', 'libs/css/*.less'],
                    dest: '<%=webAppDir%>/resources/',
                    ext: '.css'
                }]
            }
        },
        bower: {
            vendor:{
                options: {
                    keepExpandedHierarchy: false
                },
                dest: '<%=webAppDir%>/resources/vendor/',
                js_dest: '<%=webAppDir%>/resources/vendor/js',
                css_dest: '<%=webAppDir%>/resources/vendor/css',
                fonts_dest: '<%=webAppDir%>/resources/vendor/fonts'
            }
        }
    });

    grunt.registerTask("html2js_by_module", "Finds and prepares modules for concatenation.", function () {
        var html2js = {};
        grunt.file.expand([grunt.config('webAppDir') + "/resources/tmp/cache/**/templates/**"]).forEach(function (dir) {
            if(grunt.file.isFile(dir))return;
            // get the module name from the directory name
            var moduleDir = dir.substr(0, dir.lastIndexOf('/templates'));
            var moduleName = moduleDir.substr(moduleDir.lastIndexOf('/') + 1);
            // create a subtask for each module, find all src files and combine into a single js file per module
            html2js[moduleName] = {
                options: {
                    module: moduleName + "-templates",
                    base: dir + '/../../'
                },
                src: [dir + '/**/*.html'],
                dest: dir + '/../js/' +  moduleName + '.templates.js'
            };
        });
        grunt.config.set('html2js', html2js);
        grunt.task.run(['html2js']);
    });

    grunt.registerTask("concat_modules_js_by_folder", "Finds and prepares modules for concatenation.", function () {
        var concat = {};
        grunt.file.expand([grunt.config('webAppDir') + "/resources/tmp/cache/modules/**/js/**"]).forEach(function (dir) {            
            if(grunt.file.isFile(dir))return;
            // get the module name from the directory name
            var moduleDir = dir.substr(0, dir.lastIndexOf('/js'));
            var moduleName = moduleDir.substr(moduleDir.lastIndexOf('/') + 1);
            // create a subtask for each module, find all src files and combine into a single js file per module
            concat[moduleName] = {
                src: [dir + '/**/*.module.js', dir + '/**/*.js'],
                dest: '<%=webAppDir%>/resources/tmp/modules/js/' + moduleName + '.js',
                nonull: true
            };
        });
        grunt.config.set('concat', concat);
        grunt.task.run(['concat']);
    });

    grunt.registerTask("concat_libs_js_by_folder", "Finds and prepares libs for concatenation.", function () {
        var concat = {};
        grunt.file.expand([grunt.config('webAppDir') + "/resources/tmp/cache/libs/**/js/**"]).forEach(function (dir) {
            if(grunt.file.isFile(dir))return;
            // get the module name from the directory name
            var moduleDir = dir.substr(0, dir.lastIndexOf('/js'));
            var moduleName = moduleDir.substr(moduleDir.lastIndexOf('/') + 1);
            // create a subtask for each module, find all src files and combine into a single js file per module
            concat[moduleName] = {
                src: [dir + '/**/*.module.js', dir + '/**/*.js'],
                dest: '<%=webAppDir%>/resources/tmp/libs/js/' + moduleName + '.js',
                nonull: true
            };
        });
        grunt.config.set('concat', concat);
        grunt.task.run(['concat']);
    });
    
    grunt.registerTask("contact_modules_css_by_folder", "Finds and prepares modules for concatenation.", function () {
        var concat = {};
        grunt.file.expand([grunt.config('webAppDir') + "/resources/tmp/cache/modules/**/css/**"]).forEach(function (dir) {
            if(grunt.file.isFile(dir))return;
            // get the module name from the directory name
            var moduleDir = dir.substr(0, dir.lastIndexOf('/css'));
            var moduleName = moduleDir.substr(moduleDir.lastIndexOf('/') + 1);
            // create a subtask for each module, find all src files and combine into a single js file per module
            concat[moduleName] = {
                src: [dir + '/**/*.less'],
                dest: '<%=webAppDir%>/resources/tmp/modules/css/' + moduleName + '.less',
                nonull: true
            };
        });
        grunt.config.set('concat', concat);
        grunt.task.run('concat');
    });

    grunt.registerTask("contact_libs_css_by_folder", "Finds and prepares libs for concatenation.", function () {
        var concat = {};
        grunt.file.expand([grunt.config('webAppDir') + "/resources/tmp/cache/libs/**/css/**"]).forEach(function (dir) {
            if(grunt.file.isFile(dir))return;
            // get the module name from the directory name
            var moduleDir = dir.substr(0, dir.lastIndexOf('/css'));
            var moduleName = moduleDir.substr(moduleDir.lastIndexOf('/') + 1);
            // create a subtask for each module, find all src files and combine into a single js file per module
            concat[moduleName] = {
                src: [dir + '/**/*.less'],
                dest: '<%=webAppDir%>/resources/tmp/libs/css/' + moduleName + '.less',
                nonull: true
            };
        });
        grunt.config.set('concat', concat);
        grunt.task.run('concat');
    });

    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks("grunt-remove-logging");
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-html2js');
    grunt.loadNpmTasks('grunt-bower');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.registerTask('dev', ['clean:resources', 'clean:tmp', 'jshint:dev', 'copy:tmp_cache', 'html2js_by_module', 'concat_modules_js_by_folder', 'concat_libs_js_by_folder', 'ngAnnotate:tmp', 'copy:fake_js_min', 'contact_modules_css_by_folder', 'contact_libs_css_by_folder', 'less:resources', 'bower:vendor', 'clean:tmp']);
    grunt.registerTask('prod', ['clean:resources', 'clean:tmp', 'jshint:dev', 'copy:tmp_cache', 'html2js_by_module', 'concat_modules_js_by_folder', 'concat_libs_js_by_folder', 'ngAnnotate:tmp', 'uglify:resources', 'removelogging:resources', 'contact_modules_css_by_folder', 'contact_libs_css_by_folder', 'less:resources', 'clean:vendor', 'bower:vendor', 'clean:tmp']);
    grunt.registerTask('default', ['watch:dev']);
};
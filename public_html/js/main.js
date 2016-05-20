require.config({
    baseUrl: "js",
    paths: {
        jquery: "lib/jquery",
        underscore: "lib/underscore",
        backbone: "lib/backbone"
    },
    shim: {
        'backbone': {
            deps: ['underscore', 'jquery'],
            exports: 'Backbone'
        },
        'underscore': {
            exports: '_'
        }
    }
});

define([
    'backbone',
    'router'
], function(
    Backbone,
    router
){
    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register('/service_worker.js').then(function(reg) {
            console.log('Registration succeeded. Scope is ' + reg.scope);
        }).catch(function(error) {
            console.log('Registration failed with ' + error);
        });
    }
    Backbone.history.start();
});

/**
 * Created by danil on 09.05.16.
 */
var CACHE = 'v3';
this.addEventListener('install', function(event) {
    event.waitUntil(
        caches.open(CACHE).then(function(cache) {
            return cache.addAll([
                '/index.html',
                '/favicon.ico',
                '/css/main.css',
                '/img/castle.png',
                '/img/back.png',
                '/js/lib/require.js',
                'js/router.js',
                'js/main.js'
            ]);
        })
    );
});

this.addEventListener('fetch', function(event) {
    event.respondWith(
        fetch(event.request).then(function(response) {
            caches.open(CACHE).then(function(cache) {
                cache.put(event.request, response.clone());
            });
            return response;
        }).catch(function(error) {
            return caches.match(event.request);
        })
    );
});

this.addEventListener('activate', function(event) {
    var cache_white_list = [CACHE];
    console.log('activate');
    event.waitUntil(
        caches.keys().then(function(keyList) {
            return Promise.all(keyList.map(function(key) {
                if (cacheWhitelist.indexOf(key) === -1) {
                    return caches.delete(key);
                }
            }));
        })
    );
});
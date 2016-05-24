/**
 * Created by danil on 13.03.16.
 */
define([
    'backbone'
], function(
    Backbone
){
    var DEFAULTS = {
        'timeout': 3000,
        'error_block_selector':'.b-error-block',
    };

    var  error_message = function(options)
    {
        if( options['validation_result']) {
            for (key in DEFAULTS) {
                if(options[key] === undefined) {
                    options[key]=DEFAULTS[key];
                }
            }
            var data = options['validation_result'].data;
            var error_type = options['validation_result'].type;
            $(options['error_block_selector'] ).fadeIn()
                .css('visibility', 'visible')
                .text(options['error_templates'][error_type](data));
            window.setTimeout(function(){
                $(options['error_block_selector']).fadeOut();
            }, options['timeout']);
            return true;
        }
    };
    return error_message;
});


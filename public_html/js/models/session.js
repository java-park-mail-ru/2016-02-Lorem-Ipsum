/**
 * Created by danil on 12.03.16.
 */
define([
    'backbone',
    'models/validation_error',
    'models/user',
    'underscore'
], function(
    Backbone,
    ValidationError,
    UserModel,
    _
){
    
    var error_cb_wrapper = function (error_cb){
        return function(){
            if(error_cb) {
                var request_error = new ValidationError('SERVER_ERROR', null);
                error_cb(request_error);
            } 
        }
    };

    var success_cb_wrapper = function(success_cb){
        return function(){
            if(success_cb) {
                success_cb();
            }
        }
    };
    
    var SessionModel = Backbone.Model.extend({
        initialize: function() {
            this.user = new UserModel();
        },
        url: function(){
            return 'api/v1/session';
        },
        login: function(_login, _password, success_cb, error_cb) {
            this.save({},{
                 method: 'PUT',
                 attrs: {'login': _login, 'password': _password},
                 success: function(model,response){
                     this.user.id = response['id'];
                     this.user.fetch({
                         success: success_cb_wrapper(success_cb),
                         error: error_cb_wrapper(error_cb)
                     });
                 }.bind(this),
                 error: error_cb_wrapper(error_cb)
            });
        },
        logout: function(success_cb,error_cb) {
            this.destroy({
                 success:function(){
                     this.id = null;
                     this.user['id'] =null;
                     if(success_cb)
                        success_cb();
                 }.bind(this),
                 error:error_cb_wrapper(error_cb)
            });
        },
        register: function(_login, _password, _email, success_cb,error_cb) {
            this.save({},{
                method: 'PUT',
                attrs: {'login':_login,'password':_password, 'email':_email},
                url: 'api/v1/user',
                success: function(model,response){
                    if(success_cb)
                        success_cb();
                }.bind(this),
                error: error_cb_wrapper(error_cb)
            });
        },
        is_authinficated: function(success_cb,error_cb){
            this.fetch({
                 success: function(model,response){
                     this.user['id']=response['id'];
                     this.user.fetch({
                         success: success_cb_wrapper(success_cb),
                         error: error_cb_wrapper(error_cb)
                     });
                 }.bind(this),
                 error: error_cb_wrapper(error_cb)
            });
        }
    });

    return new SessionModel();
});
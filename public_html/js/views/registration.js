define([
    'backbone',
    'tmpl/registration',
    'utils/data_validator',
    'utils/error_message',
    'models/session',
    'views/base',
    'underscore'
], function(
    Backbone,
    tmpl,
    validate,
    error_message,
    session,
    BaseView,
    _
){
    var PASSWORD_VALIDATE_OPTIONS={'required':true,
        'type':'password', 'min_length':5};
    var EMAIL_VALIDATE_OPTIONS={'required':true,
        'type':'email' };
    var NICKNAME_OPTIONS = {'required':true,'type':'nickname', 'max_length':20};
    var VALIDATED_FIELDS ={'email':EMAIL_VALIDATE_OPTIONS,
                           'nickname':NICKNAME_OPTIONS,
                           'password':PASSWORD_VALIDATE_OPTIONS };

    var MUST_MATCH ={'password':'password2'};

    var RegistrationView = BaseView.extend({
        className:'b-registration',
        template: tmpl,
        events: {
            'submit':'submit_handler'
        },
        error_templates: {//шаблоны соощений об ошибках
            'INVALID':function(data) {
                return 'Invalid ' + data['field_name'];
            },
            'REQUIRED':function(data) {
                return  data['field_name'] + ' is required';
            },
            'MUST_MATCH':function() {
                return "Password don't match"
            },
            'SERVER_ERROR':function() {
                return "Some error on server";
            },
            'TOO LONG':function(data){
                return data['field'] +" max length is " + data['max_length'];
            },
            'TOO SHORT': function(data){
                return data['field'] +" min length is " + data['min_length'];
            }
        },
        initialize: function () {
            BaseView.prototype.initialize.call(this);
            _.bindAll(this,'registration_success','registration_error');
        },
        name:'registration',
        render: function () {
            BaseView.prototype.render.call(this);
            this.form = this.$('.js-signup-form')[0];
        },
        show: function () {
            BaseView.prototype.show.call(this);
        },
        hide: function () {
            BaseView.prototype.hide.call(this);
        },
        registration_success:function(){
            this.hide();
            Backbone.history.navigate('main', true);
        },
        registration_error:function(){
            error_message({
                'validation_result': session.request_error,
                'error_templates': this.error_templates
            });
        },
        submit_handler: function(event) {
            event.preventDefault();
            if(!error_message({'validation_result':validate(this.form.elements,VALIDATED_FIELDS, MUST_MATCH),
                                                           'error_templates':this.error_templates}))
            {
                session.register(this.form.elements['nickname'].value,
                                 this.form.elements['password'].value,
                                 this.form.elements['email'].value,
                                 this.registration_success,
                                 this.registration_error);
            }
        }
    });
    return new RegistrationView();
});
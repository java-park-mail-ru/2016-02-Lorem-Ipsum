define([
    'backbone',
    '../tmpl/main',
    'models/session',
    'views/base',
    'underscore'
], function(
    Backbone,
    tmpl,
    session,
    BaseView,
    _
){
    var MainView = BaseView.extend({
        className:'b-main',
        events: {
            'click .js-logout':'logout'
        },
        template: tmpl,
        initialize: function () {
            BaseView.prototype.initialize.call(this);
            _.bindAll(this,
                     'is_authinficated',
                     'not_authinficated');
        },
        render: function (is_auth) {
            this.$el.html(this.template({'isAuth':is_auth}) );
            //console.log(this.$el.html());
            return this;
        },
        is_authinficated:function(){
          this.render(true);
        },
        not_authinficated:function(){
            this.render(false);
        },
        show: function () {
            this.trigger('show', {},{'view_name':this.name});
            this.delegateEvents();
            this.$el.css('display','block');
            session.is_authinficated(this.is_authinficated,
                                     this.not_authinficated);
        },
        hide: function () {
            BaseView.prototype.hide.call(this);
        },
        logout:function(){
            session.logout(function(){
                this.show();
            }.bind(this));
        }
    });
    return new MainView();
});
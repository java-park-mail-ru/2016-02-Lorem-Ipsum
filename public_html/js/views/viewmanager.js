define([
    'backbone',
    'underscore'
], function(
    Backbone,
    _
){
    var ViewManager = Backbone.View.extend({
        initialize: function (views, selector) {
            this.views = {};
            this.selector = selector;
            _.bind(this.hide, this);
            _.each(views, function(view, name){
                this.add_view(view, name);
                this.listenTo(this.views[name],
                              'show',
                              this.hide);
            }, this);
        },

        add_view: function(view, name){
            this.views[name]= view;
            $( this.selector ).append(view.el);
        },
        delete_view: function(name){
            delete this.views[name];
        },
        contain_view: function(name){
            return this.views.hasOwnProperty(name);
        },
        show: function (view_name) {
            if(this.contain_view(view_name)){
                this.views[view_name].show();
            }
        },
        hide: function (event, view_name) {
            //Скрываем все вью, кроме того откуда пришло событие
            for(key in _.omit(this.views, view_name) ){
                this.views[key].hide();
            }
        }
    });
    return ViewManager;
});

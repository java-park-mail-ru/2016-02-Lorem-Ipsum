define([
    'backbone',
    'tmpl/scoreboard',
    'models/score',
    'collections/scores',
    'views/base'
], function(
    Backbone,
    tmpl,
    ScoreModel,
    ScoreCollection,
    BaseView
){


    var ScoreboardView = BaseView.extend({

        best_players :new ScoreCollection(),
        id:'scoreboard',
        name:'scoreboard',
        template: tmpl,
        events: {
            'click .js-back': 'hide'
        },
        initialize: function () {
            BaseView.prototype.initialize.call(this);
        },
        render: function () {
            this.best_players.fetch({
                success:function(){
                    this.$el.html(this.template(this.best_players.toJSON()));
                }.bind(this)
            })

        },
        show: function () {
            BaseView.prototype.show.call(this);
        },
        hide: function () {
            BaseView.prototype.hide.call(this);
        }
    });

    return new ScoreboardView();
});
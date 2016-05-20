define([
'backbone'
], function(
    Backbone
){

    var SCORE_URL ='api/v1/score';
    var ScoreModel = Backbone.Model.extend({
        url: function(){
            return (this.id)? (SCORE_URL+this.id) : SCORE_URL;
        },
        defaults:
        {
            'login': '',
            'score': 0
        },
        initialize:function(login, score){
            this.login = login;
            this.score = score;
        }

    });

    return ScoreModel;
});
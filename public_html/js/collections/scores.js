define([
    'backbone',
    'models/score'
], function(
    Backbone,
    ScoreModel
){

    var ScoreCollection = Backbone.Collection.extend({
        model:ScoreModel,
        url:function(){
            return 'api/v1/score';
        },
        comparator:function(score)
        {
            return (-score.get('score'));
        }
    });
    return  ScoreCollection;
});
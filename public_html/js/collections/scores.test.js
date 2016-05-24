/**
 * Created by danil on 03.04.16.
 */
define(function (require) {
    QUnit.module("collections/scores");

    QUnit.test("Тесты на сортировку", function () {

        var ScoreModel = require('models/score');
        var ScoreCollection = require('collections/scores');

        var MIN_RATING = 0;
        var MAX_RATING = 100000;
        var TESTS_COUNT = 10;
        var COLLECTION_SIZE = 50;

        for (var i = 0; i < TESTS_COUNT;i++){
            var best_players = new ScoreCollection();
            for(var j=0; j<COLLECTION_SIZE;j++){
                best_players.add({nickname: _.random(MIN_RATING,MAX_RATING),
                                  score: _.random(MIN_RATING,MAX_RATING)});
            }
            var is_sorted = true;
            for(j=1; j < COLLECTION_SIZE; j++){
               if(best_players[j]>best_players[j-1]){
                   is_sorted = false;
                   break;
               }
            }
            QUnit.ok(is_sorted,'test #'+(i+1));
        }
    });
});

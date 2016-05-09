/**
 * Created by danil on 12.03.16.
 */
define([
    'backbone'
], function(
    Backbone
){

    var Model = Backbone.Model.extend({
        initialize: function(_type,_data)
        {
            this.type = _type;//Тип ошибки валидации
            this.data = _data;//Связанная информация
        }

    });

    return Model;
});

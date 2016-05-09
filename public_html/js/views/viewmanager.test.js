/**
 * Created by danil on 03.04.16.
 */
define(function (require) {
    QUnit.module("views/manager");

    QUnit.test("Тесты viewmanagera", function () {
        var Backbone = require('backbone');
        var ViewManger = require('./viewmanager');
        var FakeView = Backbone.View.extend({
            name : '',
            is_visible:true,
            show:function(){
                this.trigger('show', {},{'view_name':this.name});
                this.is_visible=true;

            },
            hide:function(){
                this.is_visible=false;
            }
        });
        var DEFAULT_VIEW_COUNT=4;

        var views ={};
        for(var i=0;i<DEFAULT_VIEW_COUNT;i++){
            views['view' + i]= new FakeView();
            views['view' +i].name='view' +i;
        }
        var manager = new ViewManger(views, '.b-page');
        QUnit.ok(manager instanceof ViewManger,'Создание вью-менеджера');
        QUnit.ok(_.isEqual(manager.views,views),'Проверка правильности создания');

        var ok_contain =true;
        console.log(views);
        for(key in views){
            if(!manager.contain_view(key)){
                ok_contain = false;
                break;
            }
        }
        QUnit.ok(ok_contain,'Проверка метода contain');

        manager.add_view(new FakeView(), 'new_view');
        QUnit.ok(manager.contain_view('new_view'),'Добавление нового вью');

        manager.delete_view('new_view');
        QUnit.ok(!manager.contain_view('new_view'),'Удаление вью');


        var is_hidden = function(elem){
            return !elem.is_visible;
        };
        console.log(manager.views);
        for(view_name in manager.views)
        {
            manager.show(view_name);
            var other_views =_.omit(manager.views,view_name);
            QUnit.ok(_.every(other_views,is_hidden) && manager.views[view_name].is_visible,
                  'Отображаем '+ view_name+ ' и скрываем остальные вью');
        }
    });
});

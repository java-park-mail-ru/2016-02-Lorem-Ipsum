define([
    'backbone',
    'tmpl/game',
    'models/session',
    'models/score',
    'models/gamestate',
    'views/base',
    'underscore',
    'game/collisions_handlers',
    'game/painters',
    'utils/canvas_wrapper',


], function(
    Backbone,
    tmpl,
    session,
    ScoreModel,
    GameState,
    BaseView,
    _,
    initialize_collision_handlers,
    initialize_painters,
    Wrapper
){


    var ARROW_LEFT=37;
    var ARROW_RIGHT=39;

    var GameView = BaseView.extend({
        className: 'b-game',
        template: tmpl,
        name:'game',
        initialize: function () {
            BaseView.prototype.initialize.call(this);
            _.bindAll(this, 'keyup_handler','keydown_handler');
        },
        render: function () {
            this.$el.html(this.template());
            BaseView.prototype.render.call(this);
            this.wrapper = new Wrapper();
            this.wrapper.canvas = this.$('.js-canvas')[0];
            this.wrapper.context = this.wrapper.canvas.getContext('2d');
        },
        show: function () {
            BaseView.prototype.show.call(this);

            $(document).on('keydown',this.keydown_handler);
            $(document).on('keyup',this.keyup_handler);

            this.wrapper.create_left_bound({'coord':0});
            this.wrapper.create_right_bound({'coord':this.wrapper.canvas.width });
            this.wrapper.create_top_bound({'coord':0});
            this.wrapper.create_bottom_bound({'coord':this.wrapper.canvas.height});

            this.game_state = new GameState(this.wrapper);
            initialize_collision_handlers(this.wrapper);
            initialize_painters(this.wrapper);

            this.wrapper.run();
        },
        hide: function () {
            $(document).off('keyup');
            $(document).off('keydown');
            BaseView.prototype.hide.call(this);
            if(this.wrapper) {
                this.wrapper.stop();
                this.wrapper.clear();
            }
            if(this.game_state){
                window.clearInterval(this.game_state.intervalID);
            }
        },
        keydown_handler:function(event) {
            if(event.keyCode == ARROW_LEFT) {
                this.game_state.left();
            }
            else if(event.keyCode == ARROW_RIGHT) {
                this.game_state.right();
            }
        },
        keyup_handler:function(event) {
            this.game_state.stop_platform();
        }
    });
    return new GameView();
});
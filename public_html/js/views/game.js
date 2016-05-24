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
    'utils/canvas_wrapper'

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
        id: 'game',
        name:'game',
        events: {
            'click .b-scoreboard-table__elem' : 'start'
        },
        template: tmpl,
        initialize: function () {
            BaseView.prototype.initialize.call(this);
            _.bindAll(this, 'keyup_handler','keydown_handler');//,'render','gamestart_handler');
        },
        render: function () {
            console.log('In render free users:', this.game_state.free_users);
            this.$el.html(this.template (this.game_state) );
            if(this.game_state.is_running){
                this.wrapper = new Wrapper( this.$('.js-canvas')[0] );
            }
        },
        show: function () {
            //BaseView.prototype.show.call(this);
            this.trigger('show', {}, {'view_name': this.name});
            this.delegateEvents();
            this.game_state = new GameState();
            this.render();
            this.$el.css('display', 'block');
            this.listenTo(this.game_state,'gamestart',this.gamestart_handler);
            this.listenTo(this.game_state,'freeusers',this.render);

        },
        hide: function () {
            $(document).off('keyup.game', this.keyup_handler);
            $(document).off('keydown.game', this.keydown_handler);

            BaseView.prototype.hide.call(this);

            if(this.wrapper) {
                this.wrapper.stop();
                this.wrapper.clear();
            }

            if(this.game_state){
                this.game_state.end_game();
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
        },
        start:function(event){
            var enemy = event.target.textContent;
            this.game_state.invite(enemy);
            this.game_state.trigger_start();
        },
        gamestart_handler: function(){
            this.game_state.is_running = true;
            this.render();
            this.wrapper.create_left_bound({'coord':0});
            this.wrapper.create_right_bound({'coord':this.wrapper.canvas.width });
            this.wrapper.create_top_bound({'coord':0});
            this.wrapper.create_bottom_bound({'coord':this.wrapper.canvas.height});

            $(document).on('keydown.game', this.keydown_handler);
            $(document).on('keyup.game', this.keyup_handler);
            this.game_state.start_game(this.wrapper);
            initialize_collision_handlers(this.wrapper);
            initialize_painters(this.wrapper);
            this.wrapper.run();
        }

    });
    return new GameView();
});
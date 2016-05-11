/**
 * Created by danil on 17.04.16.
 */
define([
    'backbone',
    'game/game_objects/balls',
    'game/game_objects/blocks',
    'game/game_objects/platforms'
], function(
    Backbone,
    balls_initialize,
    blocks_initialize,
    platforms_initialize
){
    var GameState = Backbone.Model.extend({
        defaults: {
            'login': '',
            'score': 0
        },
        initialize: function (wrapper) {
            this.socket = new WebSocket("ws://127.0.0.1:8090/gamesocket");
            this.blocks = blocks_initialize(wrapper);
            this.your_ball =  balls_initialize(wrapper,'your') ;
            this.your_platform = platforms_initialize(wrapper,'your');
            this.another_ball = balls_initialize(wrapper,'another') ;
            this.another_platform = platforms_initialize(wrapper,'another');
            this.socket.onopen = function(event){
                console.log('socket open');
            };
            this.socket.onmessage = function (event) {
                this.your_ball.copy(event.data.your_ball);
                this.your_platform.copy(event.data.your_platform);
                this.another_ball.copy( event.data.another_ball);
                this.another_platform.copy(event.data.another_platform);
                this.blocks.matrix = event.data.blocks;
            }.bind(this);

            this.intervalID = window.setInterval(
                    function(){
                        this.socket.send(JSON.stringify(this))
                    }.bind(this), 1000/60
                );

        },
        toJSON: function () {
            return {
                blocks: this.blocks.matrix,
                your_ball: this.your_ball,
                your_plaform: this.your_plaform,
                another_ball: this.another_ball,
                another_plaform: this.another_plaform
            };
        },
        left: function () {
            this.send_action('left');
            this.your_platform.vx = -5;
        },
        right: function () {
            this.send_action('right');
            this.your_platform.vx = 5;
        },
        stop_platform: function () {
            this.send_action('stop');
            this.your_platform.vx = 0;
        },
        send_action: function(action){
            this.socket.send(JSON.stringify({
                'action':action
            }));
        }

    });

    return GameState;
});
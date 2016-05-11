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
                var data = JSON.parse(event.data);
<<<<<<< HEAD
                console.log(data);
                //this.your_ball.copy(data.your_ball);
                //this.your_platform.copy(data.your_platform);
=======
                this.your_ball.copy(data.your_ball);
                this.your_platform.copy(data.your_platform);
>>>>>>> origin/master
                this.another_ball.copy(data.another_ball);
                this.another_platform.copy(data.another_platform);
                this.blocks.matrix = data.blocks;
            }.bind(this);

            this.intervalID = window.setInterval(
                    function(){
                        this.socket.send(JSON.stringify(this));
                        console.log('socket sends state');
                    }.bind(this), 100
                );

        },
        toJSON: function () {
            return {
                blocks: this.blocks.matrix,
                your_ball: this.your_ball,
<<<<<<< HEAD
                your_platform: this.your_platform,
                another_ball: this.another_ball,
                another_platform: this.another_platform
=======
                your_plaform: this.your_platform,
                another_ball: this.another_ball,
                another_plaform: this.another_platform
>>>>>>> origin/master
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
            this.socket.send(JSON.stringify(this);
        }

    });

    return GameState;
});

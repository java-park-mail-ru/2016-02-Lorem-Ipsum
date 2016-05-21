/**
 * Created by danil on 17.04.16.
 */
define([
    'backbone',
    'game/game_objects/balls',
    'game/game_objects/blocks',
    'game/game_objects/platforms'
], function (Backbone,
             balls_initialize,
             blocks_initialize,
             platforms_initialize) {
    var URL = 'api/v1/game';
    var GameState = Backbone.Model.extend({
        defaults: {
            'is_running': false
        },
        initialize: function (wrapper) {
            this.socket = new WebSocket('ws://127.0.0.1:8090/gamesocket');
            this.socket.onopen = function (){
                console.log('Open game socket');
                this.send_action('connect');
                this.send_action('freeusers');
                this.socket.onmessage = this.socket_message_handler.bind(this);
            }.bind(this);
            this.socket.onclose = function () {
                console.log('Close game socket');
            }.bind(this);
            this.free_users = [ 'dummy' ];

        },
        socket_message_handler: function(event){
            var data = JSON.parse(event.data);
            console.log('Recevied object:', data);
            var handlers_map = {};
            handlers_map['update_state'] = function(){
                this.your_ball.copy(data.another_ball);
                this.your_platform.copy(data.another_platform);
                this.another_ball.copy(data.another_ball);
                this.another_platform.copy(data.another_platform);
                if (data.blocks) {
                    this.blocks.matrix = data.blocks;
                }
            }.bind(this);
            handlers_map['stop'] = function(){
                this.is_running = false;
            }.bind(this);
            handlers_map['freeusers'] = function(){
                this.free_users = data.data;
                console.log(this.free_users);
                this.trigger('freeusers');
            }.bind(this);
            handlers_map['started'] = function(){
                this.trigger('started');
            }.bind(this);
            if(data.action && handlers_map[data.action]){
                handlers_map[ data.action ]();
            }
        },
        toJSON: function () {
            return {
                'blocks': this.blocks.matrix,
                'your_ball': this.your_ball,
                'your_platform': this.your_platform,
                'another_ball': this.another_ball,
                'another_platform': this.another_platform
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
        send_action: function (action,data) {
            var object_to_send = {
                'action': action,
                'data': data
            };
            if (this.socket.readyState == 1) {
                console.log('Send object:', object_to_send);
                this.socket.send(JSON.stringify(object_to_send));
            }
            else{
                console.log('Fail send object:', object_to_send, 'socket not open');
            }
        },
        trigger_start: function(){
            this.trigger('gamestart');
        },
        invite: function(enemy){
            this.send_action('start',{'enemy':enemy});
        },
        start_game: function (wrapper) {
            this.is_running = true ;
            this.blocks = blocks_initialize(wrapper);
            this.your_balls =  balls_initialize(wrapper,'your') ;
            this.your_platform = platforms_initialize(wrapper,'your');
            this.another_ball = balls_initialize(wrapper,'another') ;
            this.another_platform = platforms_initialize(wrapper,'another');
        },
        end_game: function () {

            this.send_action('disconnect');
            this.socket.close();
        }
    });

    return GameState;
});
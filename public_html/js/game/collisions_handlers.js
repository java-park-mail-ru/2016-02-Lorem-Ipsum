/**
 * Created by danil on 16.04.16.
 */
define([

], function(

){
    var is_ball = function( object){
        return (object._group === 'your_balls' )||
               (object._group === 'another_balls');
    };

    var platform_balls_handler = function(first,  second){
        var platform;
        var ball;
        if (is_ball(first))
        {
            ball = first;
            platform = second;
        }
        else{
            ball = second;
            platform = first;
        }

        ball.vy *= -1;
        var platform_center = platform.x + platform.width / 2;
        ball.vx = 3 * (ball.x - platform_center) / platform.width;

    };

    var blocks_ball_handler = function(first, second){
        var blocks;
        var ball;
        if(is_ball(first)){
            ball = first;
            blocks = second;
        }
        else{
            ball = second;
            blocks = first;
        }

        var row = Math.floor(ball.top() / (blocks.block_height + blocks.padding_y) );
        var col = Math.floor(ball.x / (blocks.block_width + blocks.padding_x) );
        if ( row < blocks.rows && row >= 0 && col >= 0 && blocks.matrix[row][col] === 1){
            blocks.matrix[row][col] = 0;
            ball.vy *= -1;
        }
    };

    var ball_bound_handler = function(ball,bound){
        if (bound.type === 'left' || bound.type === 'right') {
            ball.vx *= -1;
        }
        else if (bound.type === 'top'
            ||bound.type === 'bottom' )
        {
            ball.vy *= -1;
        }
    };

    var platform_bound_handler = function(platform,bound){
        if (bound.type === 'left') {
            platform.x = bound.coord;
        }
        else{
            platform.x = bound.coord - platform.width;
        }
    };

    function initialize_collision_handlers (Wrapper){
        Wrapper.oncollision({
            '_id': 'your_platform',
            '_group': 'your_balls',
            'handler': platform_balls_handler
        });
        Wrapper.oncollision({
            '_id': 'another_platform',
            '_group': 'another_balls',
            'handler': platform_balls_handler
        });
        Wrapper.oncollision({
            'group1': 'your_balls',
            'group2': 'blocks',
            'handler': blocks_ball_handler
        });
        Wrapper.oncollision({
            'group1': 'another_balls',
            'group2': 'blocks',
            'handler': blocks_ball_handler
        });

        Wrapper.onboundcollision({
            '_group': 'your_balls',
            'handler': ball_bound_handler
        });
        Wrapper.onboundcollision({
            '_group': 'another_balls',
            'handler': ball_bound_handler
        });
        Wrapper.onboundcollision({
            '_id': 'your_platform',
            'handler': platform_bound_handler
        });
        Wrapper.onboundcollision({
            '_id': 'another_platform',
            'handler': platform_bound_handler
        });
    }
    return initialize_collision_handlers;
});
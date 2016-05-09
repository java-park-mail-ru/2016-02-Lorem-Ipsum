/**
 * Created by danil on 16.04.16.
 */
define([
], function(

){

    var ball_png = new Image();
    ball_png.src = 'img/ball.png';
    var platform_png = new Image();
    platform_png.src = 'img/platform2.png';
    var block_png = new Image();
    block_png.src = 'img/block.png';
    var another_ball = new Image;
    another_ball.src ='img/another_ball.png';


    function painters_initialize(Wrapper ){
        Wrapper.id_draw('your_platform',function(context,platform){
            context.drawImage(platform_png, platform.x, platform.y,platform.width,platform.height);
        });
        Wrapper.id_draw('another_platform',function(context,platform){
            context.globalAlpha = 0.2;
            context.drawImage(platform_png, platform.x, platform.y,platform.width,platform.height);
            context.globalAlpha = 1;
        });
        Wrapper.group_draw('another_balls',function(context,ball){
            context.globalAlpha = 0.4;
            context.drawImage(another_ball, ball.left(), ball.top(),2*ball.radius,2*ball.radius);
            context.globalAlpha = 1;
        });
        Wrapper.group_draw('your_balls',function(context,ball){
            context.drawImage(ball_png, ball.left(), ball.top(),2*ball.radius,2*ball.radius);
        });
        Wrapper.group_draw('blocks',function(context,blocks){
            for (var i =0; i<blocks.rows; ++i){
                for (var j =0; j<blocks.columns; ++j){
                    if (blocks.matrix[i][j] === 1){
                        context.drawImage(block_png,
                                          j*(blocks.block_width + blocks.padding_x),
                                          i*(blocks.block_height +blocks.padding_y),
                                          blocks.block_width,
                                          blocks.block_height);
                    }
                }
            }
        });
    }
    return painters_initialize;

});
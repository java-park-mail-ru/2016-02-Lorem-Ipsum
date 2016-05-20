/**
 * Created by danil on 06.05.16.
 */
define( function(){

    function balls_initialize(wrapper, owner){
         var ball=  wrapper.create_ball({
            _group:owner + '_balls',
            x:wrapper.canvas.width/2,
            y:wrapper.canvas.height/2,
            vy:-2,
            vx:0,
            radius:5
        });
        return ball;
    }
    return balls_initialize;

});
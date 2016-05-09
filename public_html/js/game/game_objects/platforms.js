/**
 * Created by danil on 06.05.16.
 */
define( function() {
    var PLATFORM_OFFSET_FACTOR = 2;
    function platforms_initialize(wrapper, owner){
        var platform =  wrapper.create_rectangle({
            _id:owner+'_platform',
            x:wrapper.canvas.width/2 - 40/2,
            y:wrapper.canvas.height - PLATFORM_OFFSET_FACTOR*5,
            vy:0,
            vx:0,
            width:40,
            height:4
        });
        return platform;
    }
    return platforms_initialize;

});
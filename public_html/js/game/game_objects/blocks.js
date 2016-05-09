/**
 * Created by danil on 06.05.16.
 */
define( function() {

    var BLOCK_ROWS =4;//6;
    var BLOCK_COLUMNS =20;//20;
    function blocks_initialize(wrapper){
        var blocks = wrapper.create_rectangle({
            _group:'blocks',
            x:0,
            y:0,
            vy:0,
            vx:0,
            width:wrapper.canvas.width,
            height:(BLOCK_ROWS*(15 + 1))
        });
        blocks.padding_x = 2;
        blocks.padding_y = 1;
        blocks.block_width = 15;
        blocks.offset_x =(wrapper.canvas.width - blocks.width*BLOCK_COLUMNS)/3;
        blocks.offset_y = 15;
        blocks.columns = BLOCK_COLUMNS;
        blocks.rows = BLOCK_ROWS;
        blocks.matrix = new Array(blocks.rows);
        blocks.block_height = 15;

        for (var i =0 ; i < blocks.rows; i++){
            blocks.matrix[i] = new Array(blocks.columns);
            for(var j=0; j<blocks.columns; j++){
                blocks.matrix[i][j] = 1;
            }
        }
    }
    return blocks_initialize;

});


(function(context) {
  'use strict';

  var Timer = Java.type('java.util.Timer');
  var Phaser = Java.type('java.util.concurrent.Phaser');

  var timer = new Timer('jsEventLoop', false);
  var phaser = new Phaser();

  var onTaskFinished = function() {
    phaser.arriveAndDeregister();
  };

  context.setTimeout = function(fn, millis /* [, args...] */) {
    var args = [].slice.call(arguments, 2, arguments.length);

    var phase = phaser.register();
    var canceled = false;
    timer.schedule(function() {
      if (canceled) {
        return;
      }

      try {
        fn.apply(context, args);
      } catch (e) {
        print(e);
      } finally {
        onTaskFinished();
      }
    }, millis);

    return function() {
      onTaskFinished();
      canceled = true;
    };
  };

  context.clearTimeout = function(cancel) {
    cancel();
  };

  context.setInterval = function(fn, delay /* [, args...] */) {
    var args = [].slice.call(arguments, 2, arguments.length);

    var cancel = null;

    var loop = function() {
      cancel = context.setTimeout(loop, delay);
      fn.apply(context, args);
    };

    cancel = context.setTimeout(loop, delay);
    return function() {
      cancel();
    };
  };

  context.clearInterval = function(cancel) {
    cancel();
  };

})(this);

var condition = {
    myId : 0,
    enemyId : 0,
    x : 0,
    score : 0,
    random : 0
};

var funLoop = function(data) {
    var input = JSON.parse(data);
    for(;;) {
        setTimeout(function() {
            condition.random = Math.random() * 10;
        }, 100)
    }
}

var state = function(data) {
    return JSON.stringify(condition);
}

var start = function(data) {
    var input = JSON.parse(data);
    condition.myId = input["myId"];
    condition.enemyId = input["enemyId"];
    funLoop(data);
}

var move = function() {
    condition.x += 5;
    condition.score += Math.random() * 10;
    var result = {
            x : condition.x,
            score : condition.score,
            message : "enemy movement",
            sendToEnemy : true
        };
    return JSON.stringify(result);

}

var score = function() {
    result = {
        score : condition.score
    };
    return JSON.stringify(result);
}

var check = function(data) {
    var input = JSON.parse(data);
    var enemyScore = input["score"];
    var won;
    if(condition.score > 15) {
        won = true;
    }
    else {
        won = false;
    }
    var result = {
        "res" : won
    }
    return JSON.stringify(result);
}


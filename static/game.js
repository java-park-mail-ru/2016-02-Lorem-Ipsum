<html>
<head>
    <meta charset="UTF-8"/>
    <title>Game</title>
    <script type="text/javascript">
        var ws;
        var started = false;
        var finished = false;

        var myName = "${myName}";
        var enemyName = "";

        init = function () {
            ws = new WebSocket("ws://127.0.0.1:9090/game");

            ws.onopen = function (event) {

            }

            ws.onmessage = function (event) {
                var data = JSON.parse(event.data);
                if(data.status == "start"){
                    document.getElementById("wait").style.display = "none";
                    document.getElementById("gameplay").style.display = "block";
                    document.getElementById("enemyName").innerHTML = data.enemyName;
                }

                if(data.status == "finish"){
                   document.getElementById("gameOver").style.display = "block";
                   document.getElementById("gameplay").style.display = "none";

                   if(data.win)
                        document.getElementById("win").innerHTML = "winner!";
                   else
                        document.getElementById("win").innerHTML = "loser!";
                }

                if(data.status == "increment" && data.name == "${myName}"){
                    document.getElementById("myScore").innerHTML = data.score;
                }

                if(data.status == "increment" && data.name == document.getElementById("enemyName").innerHTML){
                    document.getElementById("enemyScore").innerHTML = data.score;
                }
            }

            ws.onclose = function (event) {

            }

        };

        function sendMessage() {
            var message = "{\"type\" : \"\"}";
            ws.send(message);
        }

    </script>
</head>
<body onload="init();">
<div id="body">
    <div id="hello">
        <p>Hello, ${myName}!</p>
    </div>

    <div id="wait">
        <p>Prepare yourself. Wait for enemy!</p>
    </div>

    <div id="gameplay" style="display: none">
        <div id="score">
            <p>${myName}: <span id="myScore">0</span></p>

            <p><span id="enemyName"></span>: <span id="enemyScore">0</span></p>
        </div>

        <form name="gamePlay" action="">
            <input type="button" name="submit" value="Press to win!" onclick="sendMessage();"/>
        </form>
    </div>

    <div id="gameOver" style="display: none">
        <p>Game over! You are <span id="win"></span></p>
    </div>

</div>
</body>
</html>
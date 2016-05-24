define([
    'backbone',
    'views/main',
    'views/scoreboard',
    'views/login',
    'views/game',
    'views/registration',
    'views/viewmanager',
    'models/session'
], function(
    Backbone,
    MainView,
    ScoreboardView,
    LoginView,
    GameView,
    RegistrationView,
    ViewManager,
    session
){

    var views ={main:MainView,
                scoreboard:ScoreboardView,
                game:GameView,
                login:LoginView,
                registration:RegistrationView
    };
    var manager = new ViewManager(views, '#page');
    var Router = Backbone.Router.extend({
        routes: {
            'main':'mainAction',
            'scoreboard': 'scoreboardAction',
            'game': 'gameAction',
            'login': 'loginAction',
            'registration': 'registrationAction',
            '*default': 'defaultActions'
        },
        mainAction: function() {
            manager.show('main');
        },
        scoreboardAction: function () {
            manager.show('scoreboard');
        },
        gameAction: function () {
            session.is_authinficated(function(){
                manager.show('game');
            }, function(){
                Backbone.history.navigate();
            });
        },
        loginAction: function () {
            manager.show('login');
        },
        registrationAction: function() {
            manager.show('registration');
        },
        defaultActions: function () {
            this.mainAction();
        }
    });
    return new Router();
});
/**
 * Created by danil on 03.04.16.
 */
define(function (require) {
    QUnit.module("utils/data_validator");

    QUnit.test("Тесты валидации", function () {

        var validate = require('utils/data_validator');
        var ValidationError = require('models/validation_error');
        var PASSWORD_VALIDATE_OPTIONS={'required':true,
            'type':'password', 'min_length':5};
        var EMAIL_VALIDATE_OPTIONS={'required':true,
            'type':'email' };
        var NICKNAME_OPTIONS = {'required':true,'type':'nickname', 'max_length':20};
        var VALIDATED_FIELDS ={'email':EMAIL_VALIDATE_OPTIONS,
            'nickname':NICKNAME_OPTIONS,
            'password':PASSWORD_VALIDATE_OPTIONS };
        var MUST_MATCH ={'password':'password2'};

        var form_data = {email:{value:''},
            nickname:{value:''},
            password:{value:''},
            password2:{value:''}
        };
        var aux_validate = function(){//Cокрщённая запись
            return validate(form_data, VALIDATED_FIELDS, MUST_MATCH);
        };
        var check_equals = function(type,data){
            var validation_result = validate(form_data, VALIDATED_FIELDS, MUST_MATCH);
            return (validation_result.type === type && _.isEqual(validation_result.data, data) );
        };

        QUnit.ok(check_equals('REQUIRED',{'field_name':'email'}),'Все поля пустые');

        form_data.email.value = '5718658793';//невалидный email
        QUnit.ok(check_equals('INVALID',{'field_name':'email'}),'Невалидный email');

        form_data.email.value = 'valid@email.ru';//Валидный email
        QUnit.ok(check_equals('REQUIRED',{'field_name':'nickname'}),'Требуется nickname');

        form_data.nickname.value  ='veryveryveryloooooooooooooooooooooongname';
        QUnit.ok(check_equals('TOO LONG',{'field_name':'nickname', 'max_length':20}),'Слишком длинный nickname');

        form_data.nickname.value = 'validname';
        QUnit.ok(check_equals('REQUIRED',{'field_name':'password'}),'Требуется пароль');

        form_data.password.value = 'sh';
        QUnit.ok(check_equals('TOO SHORT',{'field_name':'password', 'min_length':5}),'Слишком короткий пароль');

        form_data.password.value = 'validpassword';
        QUnit.ok(check_equals('MUST_MATCH',{'password':'password2'}),'Пароли не совпадают');

        form_data.password2.value = 'validpassword';
        QUnit.equal(aux_validate(),null,'Валидные данные');

    });
});
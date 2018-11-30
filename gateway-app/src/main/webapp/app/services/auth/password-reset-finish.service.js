(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('PasswordResetFinish', PasswordResetFinish);

    PasswordResetFinish.$inject = ['$resource'];

    function PasswordResetFinish($resource) {
        var service = $resource('uaaapp/api/account/reset-password/finish', {}, {});

        return service;
    }
})();

(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Wallet', Wallet);

    Wallet.$inject = ['$resource'];

    function Wallet ($resource) {
        var resourceUrl =  'walletapp/' + 'api/wallets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

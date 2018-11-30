(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('WalletAudit', WalletAudit);

    WalletAudit.$inject = ['$resource'];

    function WalletAudit ($resource) {
        var resourceUrl =  'walletapp/' + 'api/wallet-audits/:id';

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

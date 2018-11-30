(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('WalletAuditConfig', WalletAuditConfig);

    WalletAuditConfig.$inject = ['$resource'];

    function WalletAuditConfig ($resource) {
        var resourceUrl =  'walletapp/' + 'api/wallet-audit-configs/:id';

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

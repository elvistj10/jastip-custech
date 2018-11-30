(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('WalletTransaction', WalletTransaction);

    WalletTransaction.$inject = ['$resource', 'DateUtils'];

    function WalletTransaction ($resource, DateUtils) {
        var resourceUrl =  'walletapp/' + 'api/wallet-transactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.transactionDateTime = DateUtils.convertDateTimeFromServer(data.transactionDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

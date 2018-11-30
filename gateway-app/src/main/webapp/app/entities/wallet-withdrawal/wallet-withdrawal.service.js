(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('WalletWithdrawal', WalletWithdrawal);

    WalletWithdrawal.$inject = ['$resource', 'DateUtils'];

    function WalletWithdrawal ($resource, DateUtils) {
        var resourceUrl =  'walletapp/' + 'api/wallet-withdrawals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.requestDateTime = DateUtils.convertDateTimeFromServer(data.requestDateTime);
                        data.completedDateTime = DateUtils.convertDateTimeFromServer(data.completedDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

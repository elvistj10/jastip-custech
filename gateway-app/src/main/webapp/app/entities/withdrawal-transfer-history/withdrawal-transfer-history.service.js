(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('WithdrawalTransferHistory', WithdrawalTransferHistory);

    WithdrawalTransferHistory.$inject = ['$resource'];

    function WithdrawalTransferHistory ($resource) {
        var resourceUrl =  'walletapp/' + 'api/withdrawal-transfer-histories/:id';

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

(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('WithdrawalTransferFailed', WithdrawalTransferFailed);

    WithdrawalTransferFailed.$inject = ['$resource'];

    function WithdrawalTransferFailed ($resource) {
        var resourceUrl =  'walletapp/' + 'api/withdrawal-transfer-faileds/:id';

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

(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('WithdrawalTransferList', WithdrawalTransferList);

    WithdrawalTransferList.$inject = ['$resource'];

    function WithdrawalTransferList ($resource) {
        var resourceUrl =  'walletapp/' + 'api/withdrawal-transfer-lists/:id';

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

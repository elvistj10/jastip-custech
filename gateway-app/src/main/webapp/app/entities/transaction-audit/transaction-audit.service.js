(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('TransactionAudit', TransactionAudit);

    TransactionAudit.$inject = ['$resource'];

    function TransactionAudit ($resource) {
        var resourceUrl =  'transactionapp/' + 'api/transaction-audits/:id';

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

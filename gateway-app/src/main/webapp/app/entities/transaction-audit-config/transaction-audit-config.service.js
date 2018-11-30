(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('TransactionAuditConfig', TransactionAuditConfig);

    TransactionAuditConfig.$inject = ['$resource'];

    function TransactionAuditConfig ($resource) {
        var resourceUrl =  'transactionapp/' + 'api/transaction-audit-configs/:id';

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

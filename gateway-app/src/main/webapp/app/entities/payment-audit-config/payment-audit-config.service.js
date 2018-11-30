(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('PaymentAuditConfig', PaymentAuditConfig);

    PaymentAuditConfig.$inject = ['$resource'];

    function PaymentAuditConfig ($resource) {
        var resourceUrl =  'paymentapp/' + 'api/payment-audit-configs/:id';

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

(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('PaymentAudit', PaymentAudit);

    PaymentAudit.$inject = ['$resource'];

    function PaymentAudit ($resource) {
        var resourceUrl =  'paymentapp/' + 'api/payment-audits/:id';

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

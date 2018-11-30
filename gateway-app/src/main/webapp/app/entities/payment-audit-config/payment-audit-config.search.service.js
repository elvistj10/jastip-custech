(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('PaymentAuditConfigSearch', PaymentAuditConfigSearch);

    PaymentAuditConfigSearch.$inject = ['$resource'];

    function PaymentAuditConfigSearch($resource) {
        var resourceUrl =  'paymentapp/' + 'api/_search/payment-audit-configs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

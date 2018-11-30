(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('PaymentAuditSearch', PaymentAuditSearch);

    PaymentAuditSearch.$inject = ['$resource'];

    function PaymentAuditSearch($resource) {
        var resourceUrl =  'paymentapp/' + 'api/_search/payment-audits/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

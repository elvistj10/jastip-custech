(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('PaymentTransferUnmatched', PaymentTransferUnmatched);

    PaymentTransferUnmatched.$inject = ['$resource', 'DateUtils'];

    function PaymentTransferUnmatched ($resource, DateUtils) {
        var resourceUrl =  'paymentapp/' + 'api/payment-transfer-unmatcheds/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.paymentUnmatchedDateTime = DateUtils.convertDateTimeFromServer(data.paymentUnmatchedDateTime);
                        data.checkDateTime = DateUtils.convertDateTimeFromServer(data.checkDateTime);
                        data.expiredDateTime = DateUtils.convertDateTimeFromServer(data.expiredDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

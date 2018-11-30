(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('PaymentTransferCheckList', PaymentTransferCheckList);

    PaymentTransferCheckList.$inject = ['$resource', 'DateUtils'];

    function PaymentTransferCheckList ($resource, DateUtils) {
        var resourceUrl =  'paymentapp/' + 'api/payment-transfer-check-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.paymentConfirmDateTime = DateUtils.convertDateTimeFromServer(data.paymentConfirmDateTime);
                        data.expiredDateTime = DateUtils.convertDateTimeFromServer(data.expiredDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

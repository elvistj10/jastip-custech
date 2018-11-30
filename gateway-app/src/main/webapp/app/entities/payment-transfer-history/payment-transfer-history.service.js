(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('PaymentTransferHistory', PaymentTransferHistory);

    PaymentTransferHistory.$inject = ['$resource', 'DateUtils'];

    function PaymentTransferHistory ($resource, DateUtils) {
        var resourceUrl =  'paymentapp/' + 'api/payment-transfer-histories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.paymentConfirmDateTime = DateUtils.convertDateTimeFromServer(data.paymentConfirmDateTime);
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

(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('OfferingPayment', OfferingPayment);

    OfferingPayment.$inject = ['$resource', 'DateUtils'];

    function OfferingPayment ($resource, DateUtils) {
        var resourceUrl =  'transactionapp/' + 'api/offering-payments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.paymentConfirmDateTime = DateUtils.convertDateTimeFromServer(data.paymentConfirmDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

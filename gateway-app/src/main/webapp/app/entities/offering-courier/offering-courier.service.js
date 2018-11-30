(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('OfferingCourier', OfferingCourier);

    OfferingCourier.$inject = ['$resource', 'DateUtils'];

    function OfferingCourier ($resource, DateUtils) {
        var resourceUrl =  'transactionapp/' + 'api/offering-couriers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.courierSendDate = DateUtils.convertDateTimeFromServer(data.courierSendDate);
                        data.courierEstDeliveredDate = DateUtils.convertDateTimeFromServer(data.courierEstDeliveredDate);
                        data.confirmReceivedDateTime = DateUtils.convertDateTimeFromServer(data.confirmReceivedDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

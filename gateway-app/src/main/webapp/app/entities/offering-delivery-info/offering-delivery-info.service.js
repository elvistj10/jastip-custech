(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('OfferingDeliveryInfo', OfferingDeliveryInfo);

    OfferingDeliveryInfo.$inject = ['$resource'];

    function OfferingDeliveryInfo ($resource) {
        var resourceUrl =  'transactionapp/' + 'api/offering-delivery-infos/:id';

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

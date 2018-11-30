(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('OfferingDeliveryService', OfferingDeliveryService);

    OfferingDeliveryService.$inject = ['$resource'];

    function OfferingDeliveryService ($resource) {
        var resourceUrl =  'transactionapp/' + 'api/offering-delivery-services/:id';

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

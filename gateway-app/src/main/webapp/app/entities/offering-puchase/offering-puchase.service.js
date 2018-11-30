(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('OfferingPuchase', OfferingPuchase);

    OfferingPuchase.$inject = ['$resource'];

    function OfferingPuchase ($resource) {
        var resourceUrl =  'transactionapp/' + 'api/offering-puchases/:id';

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

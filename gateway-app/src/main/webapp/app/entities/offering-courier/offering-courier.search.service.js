(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('OfferingCourierSearch', OfferingCourierSearch);

    OfferingCourierSearch.$inject = ['$resource'];

    function OfferingCourierSearch($resource) {
        var resourceUrl =  'transactionapp/' + 'api/_search/offering-couriers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

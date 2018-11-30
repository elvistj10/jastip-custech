(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('AddressSearch', AddressSearch);

    AddressSearch.$inject = ['$resource'];

    function AddressSearch($resource) {
        var resourceUrl =  'profilesapp/' + 'api/_search/addresses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

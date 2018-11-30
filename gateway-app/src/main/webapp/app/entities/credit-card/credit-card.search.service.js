(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('CreditCardSearch', CreditCardSearch);

    CreditCardSearch.$inject = ['$resource'];

    function CreditCardSearch($resource) {
        var resourceUrl =  'profilesapp/' + 'api/_search/credit-cards/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

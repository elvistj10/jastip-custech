(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('BankAccountSearch', BankAccountSearch);

    BankAccountSearch.$inject = ['$resource'];

    function BankAccountSearch($resource) {
        var resourceUrl =  'profilesapp/' + 'api/_search/bank-accounts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('PostingSearch', PostingSearch);

    PostingSearch.$inject = ['$resource'];

    function PostingSearch($resource) {
        var resourceUrl =  'transactionapp/' + 'api/_search/postings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

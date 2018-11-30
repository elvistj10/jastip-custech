(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('ProfileSearch', ProfileSearch);

    ProfileSearch.$inject = ['$resource'];

    function ProfileSearch($resource) {
        var resourceUrl =  'profilesapp/' + 'api/_search/profiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

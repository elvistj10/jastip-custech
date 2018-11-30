(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('ProfilesAuditSearch', ProfilesAuditSearch);

    ProfilesAuditSearch.$inject = ['$resource'];

    function ProfilesAuditSearch($resource) {
        var resourceUrl =  'profilesapp/' + 'api/_search/profiles-audits/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .factory('ProfilesAuditConfigSearch', ProfilesAuditConfigSearch);

    ProfilesAuditConfigSearch.$inject = ['$resource'];

    function ProfilesAuditConfigSearch($resource) {
        var resourceUrl =  'profilesapp/' + 'api/_search/profiles-audit-configs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();

(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('ProfilesAuditConfig', ProfilesAuditConfig);

    ProfilesAuditConfig.$inject = ['$resource'];

    function ProfilesAuditConfig ($resource) {
        var resourceUrl =  'profilesapp/' + 'api/profiles-audit-configs/:id';

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

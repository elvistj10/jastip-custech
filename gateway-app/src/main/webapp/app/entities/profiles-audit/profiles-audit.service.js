(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('ProfilesAudit', ProfilesAudit);

    ProfilesAudit.$inject = ['$resource'];

    function ProfilesAudit ($resource) {
        var resourceUrl =  'profilesapp/' + 'api/profiles-audits/:id';

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

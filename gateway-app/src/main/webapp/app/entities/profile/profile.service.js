(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Profile', Profile);

    Profile.$inject = ['$resource', 'DateUtils'];

    function Profile ($resource, DateUtils) {
        var resourceUrl =  'profilesapp/' + 'api/profiles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.activeDate = DateUtils.convertDateTimeFromServer(data.activeDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

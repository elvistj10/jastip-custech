(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('FollowingList', FollowingList);

    FollowingList.$inject = ['$resource', 'DateUtils'];

    function FollowingList ($resource, DateUtils) {
        var resourceUrl =  'profilesapp/' + 'api/following-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.followingDate = DateUtils.convertDateTimeFromServer(data.followingDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

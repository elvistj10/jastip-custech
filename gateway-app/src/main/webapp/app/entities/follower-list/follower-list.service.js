(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('FollowerList', FollowerList);

    FollowerList.$inject = ['$resource', 'DateUtils'];

    function FollowerList ($resource, DateUtils) {
        var resourceUrl =  'profilesapp/' + 'api/follower-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.followedDate = DateUtils.convertDateTimeFromServer(data.followedDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

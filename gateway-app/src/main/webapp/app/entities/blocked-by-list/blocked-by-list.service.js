(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('BlockedByList', BlockedByList);

    BlockedByList.$inject = ['$resource', 'DateUtils'];

    function BlockedByList ($resource, DateUtils) {
        var resourceUrl =  'profilesapp/' + 'api/blocked-by-lists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.blockDate = DateUtils.convertDateTimeFromServer(data.blockDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

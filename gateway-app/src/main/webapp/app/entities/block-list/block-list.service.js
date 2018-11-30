(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('BlockList', BlockList);

    BlockList.$inject = ['$resource', 'DateUtils'];

    function BlockList ($resource, DateUtils) {
        var resourceUrl =  'profilesapp/' + 'api/block-lists/:id';

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

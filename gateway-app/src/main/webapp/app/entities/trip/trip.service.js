(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Trip', Trip);

    Trip.$inject = ['$resource', 'DateUtils'];

    function Trip ($resource, DateUtils) {
        var resourceUrl =  'transactionapp/' + 'api/trips/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.postingDate = DateUtils.convertDateTimeFromServer(data.postingDate);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

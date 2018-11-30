(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('MServiceFee', MServiceFee);

    MServiceFee.$inject = ['$resource', 'DateUtils'];

    function MServiceFee ($resource, DateUtils) {
        var resourceUrl =  'masterapp/' + 'api/m-service-fees/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDateTime = DateUtils.convertDateTimeFromServer(data.startDateTime);
                        data.endDateTime = DateUtils.convertDateTimeFromServer(data.endDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

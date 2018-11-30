(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('ServiceFee', ServiceFee);

    ServiceFee.$inject = ['$resource', 'DateUtils'];

    function ServiceFee ($resource, DateUtils) {
        var resourceUrl =  'masterapp/' + 'api/service-fees/:id';

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

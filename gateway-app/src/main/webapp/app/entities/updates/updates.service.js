(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Updates', Updates);

    Updates.$inject = ['$resource', 'DateUtils'];

    function Updates ($resource, DateUtils) {
        var resourceUrl =  'masterapp/' + 'api/updates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.updateDateTime = DateUtils.convertDateTimeFromServer(data.updateDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

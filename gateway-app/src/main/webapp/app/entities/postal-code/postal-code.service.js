(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('PostalCode', PostalCode);

    PostalCode.$inject = ['$resource'];

    function PostalCode ($resource) {
        var resourceUrl =  'masterapp/' + 'api/postal-codes/:id';

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

(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('MCity', MCity);

    MCity.$inject = ['$resource'];

    function MCity ($resource) {
        var resourceUrl =  'masterapp/' + 'api/m-cities/:id';

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

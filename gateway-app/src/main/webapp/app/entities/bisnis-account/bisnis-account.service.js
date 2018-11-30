(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('BisnisAccount', BisnisAccount);

    BisnisAccount.$inject = ['$resource'];

    function BisnisAccount ($resource) {
        var resourceUrl =  'masterapp/' + 'api/bisnis-accounts/:id';

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

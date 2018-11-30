(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('MAccount', MAccount);

    MAccount.$inject = ['$resource'];

    function MAccount ($resource) {
        var resourceUrl =  'masterapp/' + 'api/m-accounts/:id';

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

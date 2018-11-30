(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('BusinessAccount', BusinessAccount);

    BusinessAccount.$inject = ['$resource'];

    function BusinessAccount ($resource) {
        var resourceUrl =  'masterapp/' + 'api/business-accounts/:id';

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

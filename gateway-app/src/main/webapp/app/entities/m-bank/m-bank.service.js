(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('MBank', MBank);

    MBank.$inject = ['$resource'];

    function MBank ($resource) {
        var resourceUrl =  'masterapp/' + 'api/m-banks/:id';

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

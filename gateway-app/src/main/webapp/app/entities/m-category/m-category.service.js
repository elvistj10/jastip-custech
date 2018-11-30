(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('MCategory', MCategory);

    MCategory.$inject = ['$resource'];

    function MCategory ($resource) {
        var resourceUrl =  'masterapp/' + 'api/m-categories/:id';

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

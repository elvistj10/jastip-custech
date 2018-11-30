(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('MSubCategory', MSubCategory);

    MSubCategory.$inject = ['$resource'];

    function MSubCategory ($resource) {
        var resourceUrl =  'masterapp/' + 'api/m-sub-categories/:id';

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

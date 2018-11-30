(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('ItemCategory', ItemCategory);

    ItemCategory.$inject = ['$resource'];

    function ItemCategory ($resource) {
        var resourceUrl =  'masterapp/' + 'api/item-categories/:id';

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

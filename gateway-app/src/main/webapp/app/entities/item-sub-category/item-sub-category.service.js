(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('ItemSubCategory', ItemSubCategory);

    ItemSubCategory.$inject = ['$resource'];

    function ItemSubCategory ($resource) {
        var resourceUrl =  'masterapp/' + 'api/item-sub-categories/:id';

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

(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('MProvince', MProvince);

    MProvince.$inject = ['$resource'];

    function MProvince ($resource) {
        var resourceUrl =  'masterapp/' + 'api/m-provinces/:id';

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

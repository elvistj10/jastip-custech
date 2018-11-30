(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('PackageSize', PackageSize);

    PackageSize.$inject = ['$resource'];

    function PackageSize ($resource) {
        var resourceUrl =  'masterapp/' + 'api/package-sizes/:id';

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

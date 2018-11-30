(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('MPackageSize', MPackageSize);

    MPackageSize.$inject = ['$resource'];

    function MPackageSize ($resource) {
        var resourceUrl =  'masterapp/' + 'api/m-package-sizes/:id';

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

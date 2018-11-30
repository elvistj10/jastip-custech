(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('MasterAudit', MasterAudit);

    MasterAudit.$inject = ['$resource'];

    function MasterAudit ($resource) {
        var resourceUrl =  'masterapp/' + 'api/master-audits/:id';

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

(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('MasterAuditConfig', MasterAuditConfig);

    MasterAuditConfig.$inject = ['$resource'];

    function MasterAuditConfig ($resource) {
        var resourceUrl =  'masterapp/' + 'api/master-audit-configs/:id';

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

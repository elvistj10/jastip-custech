(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('BannerAuditConfig', BannerAuditConfig);

    BannerAuditConfig.$inject = ['$resource'];

    function BannerAuditConfig ($resource) {
        var resourceUrl =  'bannerapp/' + 'api/banner-audit-configs/:id';

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

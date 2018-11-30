(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('BannerAudit', BannerAudit);

    BannerAudit.$inject = ['$resource'];

    function BannerAudit ($resource) {
        var resourceUrl =  'bannerapp/' + 'api/banner-audits/:id';

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

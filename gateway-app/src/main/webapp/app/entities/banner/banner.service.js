(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Banner', Banner);

    Banner.$inject = ['$resource', 'DateUtils'];

    function Banner ($resource, DateUtils) {
        var resourceUrl =  'bannerapp/' + 'api/banners/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

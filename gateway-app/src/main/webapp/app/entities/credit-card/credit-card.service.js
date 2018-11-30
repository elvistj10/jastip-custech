(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('CreditCard', CreditCard);

    CreditCard.$inject = ['$resource', 'DateUtils'];

    function CreditCard ($resource, DateUtils) {
        var resourceUrl =  'profilesapp/' + 'api/credit-cards/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.expireMon = DateUtils.convertDateTimeFromServer(data.expireMon);
                        data.expireYear = DateUtils.convertDateTimeFromServer(data.expireYear);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

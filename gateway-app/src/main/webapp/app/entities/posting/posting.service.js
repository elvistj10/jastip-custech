(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Posting', Posting);

    Posting.$inject = ['$resource', 'DateUtils'];

    function Posting ($resource, DateUtils) {
        var resourceUrl =  'transactionapp/' + 'api/postings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.postingDate = DateUtils.convertDateTimeFromServer(data.postingDate);
                        data.expiredDate = DateUtils.convertDateTimeFromServer(data.expiredDate);
                        data.shoppingDate = DateUtils.convertDateTimeFromServer(data.shoppingDate);
                        data.deliveryDate = DateUtils.convertDateTimeFromServer(data.deliveryDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

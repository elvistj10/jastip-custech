(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Offering', Offering);

    Offering.$inject = ['$resource', 'DateUtils'];

    function Offering ($resource, DateUtils) {
        var resourceUrl =  'transactionapp/' + 'api/offerings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.returnDate = DateUtils.convertDateTimeFromServer(data.returnDate);
                        data.sentDate = DateUtils.convertDateTimeFromServer(data.sentDate);
                        data.offeringDate = DateUtils.convertDateTimeFromServer(data.offeringDate);
                        data.offeringExpiredDate = DateUtils.convertDateTimeFromServer(data.offeringExpiredDate);
                        data.tripStartDate = DateUtils.convertDateTimeFromServer(data.tripStartDate);
                        data.tripEndDate = DateUtils.convertDateTimeFromServer(data.tripEndDate);
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

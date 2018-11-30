(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('Feedback', Feedback);

    Feedback.$inject = ['$resource', 'DateUtils'];

    function Feedback ($resource, DateUtils) {
        var resourceUrl =  'profilesapp/' + 'api/feedbacks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.feedbackDateTime = DateUtils.convertDateTimeFromServer(data.feedbackDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

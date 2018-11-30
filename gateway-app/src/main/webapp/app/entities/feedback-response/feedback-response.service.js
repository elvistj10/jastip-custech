(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('FeedbackResponse', FeedbackResponse);

    FeedbackResponse.$inject = ['$resource', 'DateUtils'];

    function FeedbackResponse ($resource, DateUtils) {
        var resourceUrl =  'profilesapp/' + 'api/feedback-responses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.responseDateTime = DateUtils.convertDateTimeFromServer(data.responseDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

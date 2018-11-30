(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('SubComment', SubComment);

    SubComment.$inject = ['$resource', 'DateUtils'];

    function SubComment ($resource, DateUtils) {
        var resourceUrl =  'transactionapp/' + 'api/sub-comments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.subCommentDateTime = DateUtils.convertDateTimeFromServer(data.subCommentDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

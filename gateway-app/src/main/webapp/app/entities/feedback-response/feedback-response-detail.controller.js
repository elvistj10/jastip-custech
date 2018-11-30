(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('FeedbackResponseDetailController', FeedbackResponseDetailController);

    FeedbackResponseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FeedbackResponse', 'Feedback'];

    function FeedbackResponseDetailController($scope, $rootScope, $stateParams, previousState, entity, FeedbackResponse, Feedback) {
        var vm = this;

        vm.feedbackResponse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:feedbackResponseUpdate', function(event, result) {
            vm.feedbackResponse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

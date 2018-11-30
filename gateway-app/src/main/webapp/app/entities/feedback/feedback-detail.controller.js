(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('FeedbackDetailController', FeedbackDetailController);

    FeedbackDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Feedback', 'FeedbackResponse', 'Profile'];

    function FeedbackDetailController($scope, $rootScope, $stateParams, previousState, entity, Feedback, FeedbackResponse, Profile) {
        var vm = this;

        vm.feedback = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:feedbackUpdate', function(event, result) {
            vm.feedback = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

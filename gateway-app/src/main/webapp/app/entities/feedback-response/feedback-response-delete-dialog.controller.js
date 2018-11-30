(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('FeedbackResponseDeleteController',FeedbackResponseDeleteController);

    FeedbackResponseDeleteController.$inject = ['$uibModalInstance', 'entity', 'FeedbackResponse'];

    function FeedbackResponseDeleteController($uibModalInstance, entity, FeedbackResponse) {
        var vm = this;

        vm.feedbackResponse = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FeedbackResponse.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('FeedbackResponseDialogController', FeedbackResponseDialogController);

    FeedbackResponseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FeedbackResponse', 'Feedback'];

    function FeedbackResponseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FeedbackResponse, Feedback) {
        var vm = this;

        vm.feedbackResponse = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.feedbacks = Feedback.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.feedbackResponse.id !== null) {
                FeedbackResponse.update(vm.feedbackResponse, onSaveSuccess, onSaveError);
            } else {
                FeedbackResponse.save(vm.feedbackResponse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:feedbackResponseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.responseDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

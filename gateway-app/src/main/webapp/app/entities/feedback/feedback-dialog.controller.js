(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('FeedbackDialogController', FeedbackDialogController);

    FeedbackDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Feedback', 'FeedbackResponse', 'Profile'];

    function FeedbackDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Feedback, FeedbackResponse, Profile) {
        var vm = this;

        vm.feedback = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.responses = FeedbackResponse.query({filter: 'feedback-is-null'});
        $q.all([vm.feedback.$promise, vm.responses.$promise]).then(function() {
            if (!vm.feedback.response || !vm.feedback.response.id) {
                return $q.reject();
            }
            return FeedbackResponse.get({id : vm.feedback.response.id}).$promise;
        }).then(function(response) {
            vm.responses.push(response);
        });
        vm.profiles = Profile.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.feedback.id !== null) {
                Feedback.update(vm.feedback, onSaveSuccess, onSaveError);
            } else {
                Feedback.save(vm.feedback, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:feedbackUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.feedbackDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

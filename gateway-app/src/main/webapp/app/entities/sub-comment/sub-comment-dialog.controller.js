(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('SubCommentDialogController', SubCommentDialogController);

    SubCommentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SubComment', 'Comment'];

    function SubCommentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SubComment, Comment) {
        var vm = this;

        vm.subComment = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.comments = Comment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.subComment.id !== null) {
                SubComment.update(vm.subComment, onSaveSuccess, onSaveError);
            } else {
                SubComment.save(vm.subComment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:subCommentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.subCommentDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

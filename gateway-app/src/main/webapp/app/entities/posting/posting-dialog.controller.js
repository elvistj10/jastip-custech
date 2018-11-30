(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PostingDialogController', PostingDialogController);

    PostingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Posting', 'Trip', 'Offering', 'Comment'];

    function PostingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Posting, Trip, Offering, Comment) {
        var vm = this;

        vm.posting = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.trips = Trip.query();
        vm.offerings = Offering.query();
        vm.comments = Comment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.posting.id !== null) {
                Posting.update(vm.posting, onSaveSuccess, onSaveError);
            } else {
                Posting.save(vm.posting, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:postingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.postingDate = false;
        vm.datePickerOpenStatus.expiredDate = false;

        vm.setPostingItemImg = function ($file, posting) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        posting.postingItemImg = base64Data;
                        posting.postingItemImgContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.shoppingDate = false;
        vm.datePickerOpenStatus.deliveryDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

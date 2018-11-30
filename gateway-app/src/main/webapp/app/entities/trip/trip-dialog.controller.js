(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TripDialogController', TripDialogController);

    TripDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Trip', 'Posting'];

    function TripDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Trip, Posting) {
        var vm = this;

        vm.trip = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.postings = Posting.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.trip.id !== null) {
                Trip.update(vm.trip, onSaveSuccess, onSaveError);
            } else {
                Trip.save(vm.trip, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:tripUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.postingDate = false;
        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

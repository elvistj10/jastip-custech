(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingDialogController', OfferingDialogController);

    OfferingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Offering', 'Posting', 'OfferingDeliveryInfo', 'OfferingDeliveryService', 'OfferingPayment', 'OfferingPuchase', 'OfferingCourier'];

    function OfferingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Offering, Posting, OfferingDeliveryInfo, OfferingDeliveryService, OfferingPayment, OfferingPuchase, OfferingCourier) {
        var vm = this;

        vm.offering = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.postings = Posting.query();
        vm.offeringdeliveryinfos = OfferingDeliveryInfo.query();
        vm.offeringdeliveryservices = OfferingDeliveryService.query();
        vm.offeringpayments = OfferingPayment.query();
        vm.offeringpuchases = OfferingPuchase.query();
        vm.offeringcouriers = OfferingCourier.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.offering.id !== null) {
                Offering.update(vm.offering, onSaveSuccess, onSaveError);
            } else {
                Offering.save(vm.offering, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:offeringUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.returnDate = false;
        vm.datePickerOpenStatus.sentDate = false;
        vm.datePickerOpenStatus.offeringDate = false;
        vm.datePickerOpenStatus.offeringExpiredDate = false;
        vm.datePickerOpenStatus.tripStartDate = false;
        vm.datePickerOpenStatus.tripEndDate = false;
        vm.datePickerOpenStatus.shoppingDate = false;
        vm.datePickerOpenStatus.deliveryDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

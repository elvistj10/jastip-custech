(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingPaymentDialogController', OfferingPaymentDialogController);

    OfferingPaymentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'OfferingPayment', 'Offering'];

    function OfferingPaymentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, OfferingPayment, Offering) {
        var vm = this;

        vm.offeringPayment = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.offerings = Offering.query({filter: 'offeringpayment-is-null'});
        $q.all([vm.offeringPayment.$promise, vm.offerings.$promise]).then(function() {
            if (!vm.offeringPayment.offering || !vm.offeringPayment.offering.id) {
                return $q.reject();
            }
            return Offering.get({id : vm.offeringPayment.offering.id}).$promise;
        }).then(function(offering) {
            vm.offerings.push(offering);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.offeringPayment.id !== null) {
                OfferingPayment.update(vm.offeringPayment, onSaveSuccess, onSaveError);
            } else {
                OfferingPayment.save(vm.offeringPayment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:offeringPaymentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.paymentConfirmDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

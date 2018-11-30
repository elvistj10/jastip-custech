(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingCourierDialogController', OfferingCourierDialogController);

    OfferingCourierDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'OfferingCourier', 'Offering'];

    function OfferingCourierDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, OfferingCourier, Offering) {
        var vm = this;

        vm.offeringCourier = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.offerings = Offering.query({filter: 'offeringcourier-is-null'});
        $q.all([vm.offeringCourier.$promise, vm.offerings.$promise]).then(function() {
            if (!vm.offeringCourier.offering || !vm.offeringCourier.offering.id) {
                return $q.reject();
            }
            return Offering.get({id : vm.offeringCourier.offering.id}).$promise;
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
            if (vm.offeringCourier.id !== null) {
                OfferingCourier.update(vm.offeringCourier, onSaveSuccess, onSaveError);
            } else {
                OfferingCourier.save(vm.offeringCourier, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:offeringCourierUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setCourierReceiptImg = function ($file, offeringCourier) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        offeringCourier.courierReceiptImg = base64Data;
                        offeringCourier.courierReceiptImgContentType = $file.type;
                    });
                });
            }
        };
        vm.datePickerOpenStatus.courierSendDate = false;
        vm.datePickerOpenStatus.courierEstDeliveredDate = false;
        vm.datePickerOpenStatus.confirmReceivedDateTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

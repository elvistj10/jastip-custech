(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingPuchaseDialogController', OfferingPuchaseDialogController);

    OfferingPuchaseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'OfferingPuchase', 'Offering'];

    function OfferingPuchaseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, OfferingPuchase, Offering) {
        var vm = this;

        vm.offeringPuchase = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.offerings = Offering.query({filter: 'offeringpurchase-is-null'});
        $q.all([vm.offeringPuchase.$promise, vm.offerings.$promise]).then(function() {
            if (!vm.offeringPuchase.offering || !vm.offeringPuchase.offering.id) {
                return $q.reject();
            }
            return Offering.get({id : vm.offeringPuchase.offering.id}).$promise;
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
            if (vm.offeringPuchase.id !== null) {
                OfferingPuchase.update(vm.offeringPuchase, onSaveSuccess, onSaveError);
            } else {
                OfferingPuchase.save(vm.offeringPuchase, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:offeringPuchaseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setReceiptImg = function ($file, offeringPuchase) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        offeringPuchase.receiptImg = base64Data;
                        offeringPuchase.receiptImgContentType = $file.type;
                    });
                });
            }
        };

    }
})();

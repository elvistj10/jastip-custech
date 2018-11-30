(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingDeliveryInfoDialogController', OfferingDeliveryInfoDialogController);

    OfferingDeliveryInfoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'OfferingDeliveryInfo', 'Offering'];

    function OfferingDeliveryInfoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, OfferingDeliveryInfo, Offering) {
        var vm = this;

        vm.offeringDeliveryInfo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.offerings = Offering.query({filter: 'offeringdeliveryinfo-is-null'});
        $q.all([vm.offeringDeliveryInfo.$promise, vm.offerings.$promise]).then(function() {
            if (!vm.offeringDeliveryInfo.offering || !vm.offeringDeliveryInfo.offering.id) {
                return $q.reject();
            }
            return Offering.get({id : vm.offeringDeliveryInfo.offering.id}).$promise;
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
            if (vm.offeringDeliveryInfo.id !== null) {
                OfferingDeliveryInfo.update(vm.offeringDeliveryInfo, onSaveSuccess, onSaveError);
            } else {
                OfferingDeliveryInfo.save(vm.offeringDeliveryInfo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:offeringDeliveryInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

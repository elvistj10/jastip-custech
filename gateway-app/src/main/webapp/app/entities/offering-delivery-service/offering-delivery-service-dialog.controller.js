(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingDeliveryServiceDialogController', OfferingDeliveryServiceDialogController);

    OfferingDeliveryServiceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'OfferingDeliveryService', 'Offering'];

    function OfferingDeliveryServiceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, OfferingDeliveryService, Offering) {
        var vm = this;

        vm.offeringDeliveryService = entity;
        vm.clear = clear;
        vm.save = save;
        vm.offerings = Offering.query({filter: 'offeringdeliveryservice-is-null'});
        $q.all([vm.offeringDeliveryService.$promise, vm.offerings.$promise]).then(function() {
            if (!vm.offeringDeliveryService.offering || !vm.offeringDeliveryService.offering.id) {
                return $q.reject();
            }
            return Offering.get({id : vm.offeringDeliveryService.offering.id}).$promise;
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
            if (vm.offeringDeliveryService.id !== null) {
                OfferingDeliveryService.update(vm.offeringDeliveryService, onSaveSuccess, onSaveError);
            } else {
                OfferingDeliveryService.save(vm.offeringDeliveryService, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:offeringDeliveryServiceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

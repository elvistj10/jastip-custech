(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MProvinceDialogController', MProvinceDialogController);

    MProvinceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MProvince', 'MCountry'];

    function MProvinceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, MProvince, MCountry) {
        var vm = this;

        vm.mProvince = entity;
        vm.clear = clear;
        vm.save = save;
        vm.countries = MCountry.query({filter: 'mprovince-is-null'});
        $q.all([vm.mProvince.$promise, vm.countries.$promise]).then(function() {
            if (!vm.mProvince.country || !vm.mProvince.country.id) {
                return $q.reject();
            }
            return MCountry.get({id : vm.mProvince.country.id}).$promise;
        }).then(function(country) {
            vm.countries.push(country);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mProvince.id !== null) {
                MProvince.update(vm.mProvince, onSaveSuccess, onSaveError);
            } else {
                MProvince.save(vm.mProvince, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:mProvinceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

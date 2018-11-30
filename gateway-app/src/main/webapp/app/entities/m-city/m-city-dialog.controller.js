(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MCityDialogController', MCityDialogController);

    MCityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MCity', 'MProvince'];

    function MCityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, MCity, MProvince) {
        var vm = this;

        vm.mCity = entity;
        vm.clear = clear;
        vm.save = save;
        vm.provinces = MProvince.query({filter: 'mcity-is-null'});
        $q.all([vm.mCity.$promise, vm.provinces.$promise]).then(function() {
            if (!vm.mCity.province || !vm.mCity.province.id) {
                return $q.reject();
            }
            return MProvince.get({id : vm.mCity.province.id}).$promise;
        }).then(function(province) {
            vm.provinces.push(province);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.mCity.id !== null) {
                MCity.update(vm.mCity, onSaveSuccess, onSaveError);
            } else {
                MCity.save(vm.mCity, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:mCityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

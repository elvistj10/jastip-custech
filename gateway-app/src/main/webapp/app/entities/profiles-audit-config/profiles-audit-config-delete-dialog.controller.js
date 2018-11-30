(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ProfilesAuditConfigDeleteController',ProfilesAuditConfigDeleteController);

    ProfilesAuditConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProfilesAuditConfig'];

    function ProfilesAuditConfigDeleteController($uibModalInstance, entity, ProfilesAuditConfig) {
        var vm = this;

        vm.profilesAuditConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProfilesAuditConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

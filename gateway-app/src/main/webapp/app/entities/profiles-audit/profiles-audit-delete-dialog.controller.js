(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ProfilesAuditDeleteController',ProfilesAuditDeleteController);

    ProfilesAuditDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProfilesAudit'];

    function ProfilesAuditDeleteController($uibModalInstance, entity, ProfilesAudit) {
        var vm = this;

        vm.profilesAudit = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProfilesAudit.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

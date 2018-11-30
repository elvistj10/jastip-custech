(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TripDeleteController',TripDeleteController);

    TripDeleteController.$inject = ['$uibModalInstance', 'entity', 'Trip'];

    function TripDeleteController($uibModalInstance, entity, Trip) {
        var vm = this;

        vm.trip = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Trip.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

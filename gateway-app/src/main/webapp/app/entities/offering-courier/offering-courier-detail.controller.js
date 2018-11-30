(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingCourierDetailController', OfferingCourierDetailController);

    OfferingCourierDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'OfferingCourier', 'Offering'];

    function OfferingCourierDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, OfferingCourier, Offering) {
        var vm = this;

        vm.offeringCourier = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gatewayApp:offeringCourierUpdate', function(event, result) {
            vm.offeringCourier = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

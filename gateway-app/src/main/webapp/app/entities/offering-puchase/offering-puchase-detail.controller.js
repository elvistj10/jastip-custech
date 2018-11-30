(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('OfferingPuchaseDetailController', OfferingPuchaseDetailController);

    OfferingPuchaseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'OfferingPuchase', 'Offering'];

    function OfferingPuchaseDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, OfferingPuchase, Offering) {
        var vm = this;

        vm.offeringPuchase = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gatewayApp:offeringPuchaseUpdate', function(event, result) {
            vm.offeringPuchase = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ServiceFeeDetailController', ServiceFeeDetailController);

    ServiceFeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ServiceFee'];

    function ServiceFeeDetailController($scope, $rootScope, $stateParams, previousState, entity, ServiceFee) {
        var vm = this;

        vm.serviceFee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:serviceFeeUpdate', function(event, result) {
            vm.serviceFee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

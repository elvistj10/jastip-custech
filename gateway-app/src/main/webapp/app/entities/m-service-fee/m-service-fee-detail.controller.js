(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MServiceFeeDetailController', MServiceFeeDetailController);

    MServiceFeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MServiceFee'];

    function MServiceFeeDetailController($scope, $rootScope, $stateParams, previousState, entity, MServiceFee) {
        var vm = this;

        vm.mServiceFee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:mServiceFeeUpdate', function(event, result) {
            vm.mServiceFee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MBankDetailController', MBankDetailController);

    MBankDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MBank'];

    function MBankDetailController($scope, $rootScope, $stateParams, previousState, entity, MBank) {
        var vm = this;

        vm.mBank = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:mBankUpdate', function(event, result) {
            vm.mBank = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

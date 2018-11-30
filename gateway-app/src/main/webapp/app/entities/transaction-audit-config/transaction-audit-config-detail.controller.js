(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TransactionAuditConfigDetailController', TransactionAuditConfigDetailController);

    TransactionAuditConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TransactionAuditConfig'];

    function TransactionAuditConfigDetailController($scope, $rootScope, $stateParams, previousState, entity, TransactionAuditConfig) {
        var vm = this;

        vm.transactionAuditConfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:transactionAuditConfigUpdate', function(event, result) {
            vm.transactionAuditConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

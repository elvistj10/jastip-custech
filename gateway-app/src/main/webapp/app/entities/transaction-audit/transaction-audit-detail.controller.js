(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('TransactionAuditDetailController', TransactionAuditDetailController);

    TransactionAuditDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TransactionAudit'];

    function TransactionAuditDetailController($scope, $rootScope, $stateParams, previousState, entity, TransactionAudit) {
        var vm = this;

        vm.transactionAudit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:transactionAuditUpdate', function(event, result) {
            vm.transactionAudit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

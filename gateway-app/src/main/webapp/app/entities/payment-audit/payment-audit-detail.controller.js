(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentAuditDetailController', PaymentAuditDetailController);

    PaymentAuditDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PaymentAudit'];

    function PaymentAuditDetailController($scope, $rootScope, $stateParams, previousState, entity, PaymentAudit) {
        var vm = this;

        vm.paymentAudit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:paymentAuditUpdate', function(event, result) {
            vm.paymentAudit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

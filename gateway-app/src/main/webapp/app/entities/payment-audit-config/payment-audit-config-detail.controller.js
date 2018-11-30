(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PaymentAuditConfigDetailController', PaymentAuditConfigDetailController);

    PaymentAuditConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PaymentAuditConfig'];

    function PaymentAuditConfigDetailController($scope, $rootScope, $stateParams, previousState, entity, PaymentAuditConfig) {
        var vm = this;

        vm.paymentAuditConfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:paymentAuditConfigUpdate', function(event, result) {
            vm.paymentAuditConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

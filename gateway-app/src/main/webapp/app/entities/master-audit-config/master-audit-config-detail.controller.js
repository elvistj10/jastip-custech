(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MasterAuditConfigDetailController', MasterAuditConfigDetailController);

    MasterAuditConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MasterAuditConfig'];

    function MasterAuditConfigDetailController($scope, $rootScope, $stateParams, previousState, entity, MasterAuditConfig) {
        var vm = this;

        vm.masterAuditConfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:masterAuditConfigUpdate', function(event, result) {
            vm.masterAuditConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

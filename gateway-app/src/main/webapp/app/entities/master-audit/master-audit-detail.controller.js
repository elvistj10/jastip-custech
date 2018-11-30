(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MasterAuditDetailController', MasterAuditDetailController);

    MasterAuditDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MasterAudit'];

    function MasterAuditDetailController($scope, $rootScope, $stateParams, previousState, entity, MasterAudit) {
        var vm = this;

        vm.masterAudit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:masterAuditUpdate', function(event, result) {
            vm.masterAudit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

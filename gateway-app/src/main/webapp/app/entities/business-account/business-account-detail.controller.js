(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BusinessAccountDetailController', BusinessAccountDetailController);

    BusinessAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BusinessAccount'];

    function BusinessAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, BusinessAccount) {
        var vm = this;

        vm.businessAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:businessAccountUpdate', function(event, result) {
            vm.businessAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

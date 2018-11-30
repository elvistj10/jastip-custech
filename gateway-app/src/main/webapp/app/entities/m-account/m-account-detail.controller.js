(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MAccountDetailController', MAccountDetailController);

    MAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MAccount'];

    function MAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, MAccount) {
        var vm = this;

        vm.mAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:mAccountUpdate', function(event, result) {
            vm.mAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

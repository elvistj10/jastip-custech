(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BlockListDetailController', BlockListDetailController);

    BlockListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BlockList', 'Profile'];

    function BlockListDetailController($scope, $rootScope, $stateParams, previousState, entity, BlockList, Profile) {
        var vm = this;

        vm.blockList = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:blockListUpdate', function(event, result) {
            vm.blockList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

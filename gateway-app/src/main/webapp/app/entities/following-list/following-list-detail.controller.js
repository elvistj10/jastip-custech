(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('FollowingListDetailController', FollowingListDetailController);

    FollowingListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FollowingList', 'Profile'];

    function FollowingListDetailController($scope, $rootScope, $stateParams, previousState, entity, FollowingList, Profile) {
        var vm = this;

        vm.followingList = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:followingListUpdate', function(event, result) {
            vm.followingList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

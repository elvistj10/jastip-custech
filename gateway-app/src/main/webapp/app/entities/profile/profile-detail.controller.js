(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('ProfileDetailController', ProfileDetailController);

    ProfileDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Profile', 'Address', 'BankAccount', 'CreditCard', 'FollowingList', 'FollowerList', 'BlockList', 'BlockedByList', 'Feedback'];

    function ProfileDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Profile, Address, BankAccount, CreditCard, FollowingList, FollowerList, BlockList, BlockedByList, Feedback) {
        var vm = this;

        vm.profile = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gatewayApp:profileUpdate', function(event, result) {
            vm.profile = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PostingDetailController', PostingDetailController);

    PostingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Posting', 'Trip', 'Offering', 'Comment'];

    function PostingDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Posting, Trip, Offering, Comment) {
        var vm = this;

        vm.posting = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gatewayApp:postingUpdate', function(event, result) {
            vm.posting = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

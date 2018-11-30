(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MPackageSizeDetailController', MPackageSizeDetailController);

    MPackageSizeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'MPackageSize'];

    function MPackageSizeDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, MPackageSize) {
        var vm = this;

        vm.mPackageSize = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gatewayApp:mPackageSizeUpdate', function(event, result) {
            vm.mPackageSize = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PackageSizeDetailController', PackageSizeDetailController);

    PackageSizeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'PackageSize'];

    function PackageSizeDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, PackageSize) {
        var vm = this;

        vm.packageSize = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gatewayApp:packageSizeUpdate', function(event, result) {
            vm.packageSize = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

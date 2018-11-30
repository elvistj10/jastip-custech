(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MCategoryDetailController', MCategoryDetailController);

    MCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'MCategory', 'MSubCategory'];

    function MCategoryDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, MCategory, MSubCategory) {
        var vm = this;

        vm.mCategory = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gatewayApp:mCategoryUpdate', function(event, result) {
            vm.mCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('MSubCategoryDetailController', MSubCategoryDetailController);

    MSubCategoryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'MSubCategory', 'MCategory'];

    function MSubCategoryDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, MSubCategory, MCategory) {
        var vm = this;

        vm.mSubCategory = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gatewayApp:mSubCategoryUpdate', function(event, result) {
            vm.mSubCategory = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

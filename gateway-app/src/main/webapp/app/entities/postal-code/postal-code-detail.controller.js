(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('PostalCodeDetailController', PostalCodeDetailController);

    PostalCodeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PostalCode', 'City'];

    function PostalCodeDetailController($scope, $rootScope, $stateParams, previousState, entity, PostalCode, City) {
        var vm = this;

        vm.postalCode = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:postalCodeUpdate', function(event, result) {
            vm.postalCode = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

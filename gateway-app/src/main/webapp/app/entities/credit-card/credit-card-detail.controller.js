(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('CreditCardDetailController', CreditCardDetailController);

    CreditCardDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CreditCard', 'Profile'];

    function CreditCardDetailController($scope, $rootScope, $stateParams, previousState, entity, CreditCard, Profile) {
        var vm = this;

        vm.creditCard = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:creditCardUpdate', function(event, result) {
            vm.creditCard = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

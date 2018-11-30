(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('credit-card', {
            parent: 'entity',
            url: '/credit-card',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CreditCards'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/credit-card/credit-cards.html',
                    controller: 'CreditCardController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('credit-card-detail', {
            parent: 'credit-card',
            url: '/credit-card/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'CreditCard'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/credit-card/credit-card-detail.html',
                    controller: 'CreditCardDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'CreditCard', function($stateParams, CreditCard) {
                    return CreditCard.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'credit-card',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('credit-card-detail.edit', {
            parent: 'credit-card-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/credit-card/credit-card-dialog.html',
                    controller: 'CreditCardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CreditCard', function(CreditCard) {
                            return CreditCard.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('credit-card.new', {
            parent: 'credit-card',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/credit-card/credit-card-dialog.html',
                    controller: 'CreditCardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ccNumber: null,
                                cvc: null,
                                expireMon: null,
                                expireYear: null,
                                defaultCreditCard: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('credit-card', null, { reload: 'credit-card' });
                }, function() {
                    $state.go('credit-card');
                });
            }]
        })
        .state('credit-card.edit', {
            parent: 'credit-card',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/credit-card/credit-card-dialog.html',
                    controller: 'CreditCardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CreditCard', function(CreditCard) {
                            return CreditCard.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('credit-card', null, { reload: 'credit-card' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('credit-card.delete', {
            parent: 'credit-card',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/credit-card/credit-card-delete-dialog.html',
                    controller: 'CreditCardDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CreditCard', function(CreditCard) {
                            return CreditCard.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('credit-card', null, { reload: 'credit-card' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

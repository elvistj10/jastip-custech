(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bisnis-account', {
            parent: 'entity',
            url: '/bisnis-account',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BisnisAccounts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bisnis-account/bisnis-accounts.html',
                    controller: 'BisnisAccountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('bisnis-account-detail', {
            parent: 'bisnis-account',
            url: '/bisnis-account/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BisnisAccount'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bisnis-account/bisnis-account-detail.html',
                    controller: 'BisnisAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'BisnisAccount', function($stateParams, BisnisAccount) {
                    return BisnisAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bisnis-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bisnis-account-detail.edit', {
            parent: 'bisnis-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bisnis-account/bisnis-account-dialog.html',
                    controller: 'BisnisAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BisnisAccount', function(BisnisAccount) {
                            return BisnisAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bisnis-account.new', {
            parent: 'bisnis-account',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bisnis-account/bisnis-account-dialog.html',
                    controller: 'BisnisAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                coorperateId: null,
                                accountNumber: null,
                                bankName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bisnis-account', null, { reload: 'bisnis-account' });
                }, function() {
                    $state.go('bisnis-account');
                });
            }]
        })
        .state('bisnis-account.edit', {
            parent: 'bisnis-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bisnis-account/bisnis-account-dialog.html',
                    controller: 'BisnisAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BisnisAccount', function(BisnisAccount) {
                            return BisnisAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bisnis-account', null, { reload: 'bisnis-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bisnis-account.delete', {
            parent: 'bisnis-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bisnis-account/bisnis-account-delete-dialog.html',
                    controller: 'BisnisAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BisnisAccount', function(BisnisAccount) {
                            return BisnisAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bisnis-account', null, { reload: 'bisnis-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

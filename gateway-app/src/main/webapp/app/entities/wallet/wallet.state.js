(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wallet', {
            parent: 'entity',
            url: '/wallet',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Wallets'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wallet/wallets.html',
                    controller: 'WalletController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('wallet-detail', {
            parent: 'wallet',
            url: '/wallet/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Wallet'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wallet/wallet-detail.html',
                    controller: 'WalletDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Wallet', function($stateParams, Wallet) {
                    return Wallet.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wallet',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wallet-detail.edit', {
            parent: 'wallet-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet/wallet-dialog.html',
                    controller: 'WalletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Wallet', function(Wallet) {
                            return Wallet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wallet.new', {
            parent: 'wallet',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet/wallet-dialog.html',
                    controller: 'WalletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ownerId: null,
                                balance: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wallet', null, { reload: 'wallet' });
                }, function() {
                    $state.go('wallet');
                });
            }]
        })
        .state('wallet.edit', {
            parent: 'wallet',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet/wallet-dialog.html',
                    controller: 'WalletDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Wallet', function(Wallet) {
                            return Wallet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wallet', null, { reload: 'wallet' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wallet.delete', {
            parent: 'wallet',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet/wallet-delete-dialog.html',
                    controller: 'WalletDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Wallet', function(Wallet) {
                            return Wallet.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wallet', null, { reload: 'wallet' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

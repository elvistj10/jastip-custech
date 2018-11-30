(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wallet-transaction', {
            parent: 'entity',
            url: '/wallet-transaction',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WalletTransactions'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wallet-transaction/wallet-transactions.html',
                    controller: 'WalletTransactionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('wallet-transaction-detail', {
            parent: 'wallet-transaction',
            url: '/wallet-transaction/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WalletTransaction'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wallet-transaction/wallet-transaction-detail.html',
                    controller: 'WalletTransactionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WalletTransaction', function($stateParams, WalletTransaction) {
                    return WalletTransaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wallet-transaction',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wallet-transaction-detail.edit', {
            parent: 'wallet-transaction-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-transaction/wallet-transaction-dialog.html',
                    controller: 'WalletTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WalletTransaction', function(WalletTransaction) {
                            return WalletTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wallet-transaction.new', {
            parent: 'wallet-transaction',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-transaction/wallet-transaction-dialog.html',
                    controller: 'WalletTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                transactionDateTime: null,
                                transactionType: null,
                                nominal: null,
                                postingId: null,
                                offeringId: null,
                                withdrawalId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wallet-transaction', null, { reload: 'wallet-transaction' });
                }, function() {
                    $state.go('wallet-transaction');
                });
            }]
        })
        .state('wallet-transaction.edit', {
            parent: 'wallet-transaction',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-transaction/wallet-transaction-dialog.html',
                    controller: 'WalletTransactionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WalletTransaction', function(WalletTransaction) {
                            return WalletTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wallet-transaction', null, { reload: 'wallet-transaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wallet-transaction.delete', {
            parent: 'wallet-transaction',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-transaction/wallet-transaction-delete-dialog.html',
                    controller: 'WalletTransactionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WalletTransaction', function(WalletTransaction) {
                            return WalletTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wallet-transaction', null, { reload: 'wallet-transaction' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wallet-withdrawal', {
            parent: 'entity',
            url: '/wallet-withdrawal',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WalletWithdrawals'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wallet-withdrawal/wallet-withdrawals.html',
                    controller: 'WalletWithdrawalController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('wallet-withdrawal-detail', {
            parent: 'wallet-withdrawal',
            url: '/wallet-withdrawal/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WalletWithdrawal'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wallet-withdrawal/wallet-withdrawal-detail.html',
                    controller: 'WalletWithdrawalDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WalletWithdrawal', function($stateParams, WalletWithdrawal) {
                    return WalletWithdrawal.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wallet-withdrawal',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wallet-withdrawal-detail.edit', {
            parent: 'wallet-withdrawal-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-withdrawal/wallet-withdrawal-dialog.html',
                    controller: 'WalletWithdrawalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WalletWithdrawal', function(WalletWithdrawal) {
                            return WalletWithdrawal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wallet-withdrawal.new', {
            parent: 'wallet-withdrawal',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-withdrawal/wallet-withdrawal-dialog.html',
                    controller: 'WalletWithdrawalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                ownerId: null,
                                requestDateTime: null,
                                nominal: null,
                                destBankName: null,
                                destBankAccount: null,
                                status: null,
                                completedDateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wallet-withdrawal', null, { reload: 'wallet-withdrawal' });
                }, function() {
                    $state.go('wallet-withdrawal');
                });
            }]
        })
        .state('wallet-withdrawal.edit', {
            parent: 'wallet-withdrawal',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-withdrawal/wallet-withdrawal-dialog.html',
                    controller: 'WalletWithdrawalDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WalletWithdrawal', function(WalletWithdrawal) {
                            return WalletWithdrawal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wallet-withdrawal', null, { reload: 'wallet-withdrawal' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wallet-withdrawal.delete', {
            parent: 'wallet-withdrawal',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-withdrawal/wallet-withdrawal-delete-dialog.html',
                    controller: 'WalletWithdrawalDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WalletWithdrawal', function(WalletWithdrawal) {
                            return WalletWithdrawal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wallet-withdrawal', null, { reload: 'wallet-withdrawal' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

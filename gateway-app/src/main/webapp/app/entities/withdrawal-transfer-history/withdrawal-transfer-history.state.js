(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('withdrawal-transfer-history', {
            parent: 'entity',
            url: '/withdrawal-transfer-history',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WithdrawalTransferHistories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/withdrawal-transfer-history/withdrawal-transfer-histories.html',
                    controller: 'WithdrawalTransferHistoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('withdrawal-transfer-history-detail', {
            parent: 'withdrawal-transfer-history',
            url: '/withdrawal-transfer-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WithdrawalTransferHistory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/withdrawal-transfer-history/withdrawal-transfer-history-detail.html',
                    controller: 'WithdrawalTransferHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WithdrawalTransferHistory', function($stateParams, WithdrawalTransferHistory) {
                    return WithdrawalTransferHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'withdrawal-transfer-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('withdrawal-transfer-history-detail.edit', {
            parent: 'withdrawal-transfer-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/withdrawal-transfer-history/withdrawal-transfer-history-dialog.html',
                    controller: 'WithdrawalTransferHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WithdrawalTransferHistory', function(WithdrawalTransferHistory) {
                            return WithdrawalTransferHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('withdrawal-transfer-history.new', {
            parent: 'withdrawal-transfer-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/withdrawal-transfer-history/withdrawal-transfer-history-dialog.html',
                    controller: 'WithdrawalTransferHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                withdrawalId: null,
                                nominal: null,
                                destBankName: null,
                                destBankAccount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('withdrawal-transfer-history', null, { reload: 'withdrawal-transfer-history' });
                }, function() {
                    $state.go('withdrawal-transfer-history');
                });
            }]
        })
        .state('withdrawal-transfer-history.edit', {
            parent: 'withdrawal-transfer-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/withdrawal-transfer-history/withdrawal-transfer-history-dialog.html',
                    controller: 'WithdrawalTransferHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WithdrawalTransferHistory', function(WithdrawalTransferHistory) {
                            return WithdrawalTransferHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('withdrawal-transfer-history', null, { reload: 'withdrawal-transfer-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('withdrawal-transfer-history.delete', {
            parent: 'withdrawal-transfer-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/withdrawal-transfer-history/withdrawal-transfer-history-delete-dialog.html',
                    controller: 'WithdrawalTransferHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WithdrawalTransferHistory', function(WithdrawalTransferHistory) {
                            return WithdrawalTransferHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('withdrawal-transfer-history', null, { reload: 'withdrawal-transfer-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('withdrawal-transfer-failed', {
            parent: 'entity',
            url: '/withdrawal-transfer-failed',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WithdrawalTransferFaileds'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/withdrawal-transfer-failed/withdrawal-transfer-faileds.html',
                    controller: 'WithdrawalTransferFailedController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('withdrawal-transfer-failed-detail', {
            parent: 'withdrawal-transfer-failed',
            url: '/withdrawal-transfer-failed/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WithdrawalTransferFailed'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/withdrawal-transfer-failed/withdrawal-transfer-failed-detail.html',
                    controller: 'WithdrawalTransferFailedDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WithdrawalTransferFailed', function($stateParams, WithdrawalTransferFailed) {
                    return WithdrawalTransferFailed.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'withdrawal-transfer-failed',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('withdrawal-transfer-failed-detail.edit', {
            parent: 'withdrawal-transfer-failed-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/withdrawal-transfer-failed/withdrawal-transfer-failed-dialog.html',
                    controller: 'WithdrawalTransferFailedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WithdrawalTransferFailed', function(WithdrawalTransferFailed) {
                            return WithdrawalTransferFailed.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('withdrawal-transfer-failed.new', {
            parent: 'withdrawal-transfer-failed',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/withdrawal-transfer-failed/withdrawal-transfer-failed-dialog.html',
                    controller: 'WithdrawalTransferFailedDialogController',
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
                    $state.go('withdrawal-transfer-failed', null, { reload: 'withdrawal-transfer-failed' });
                }, function() {
                    $state.go('withdrawal-transfer-failed');
                });
            }]
        })
        .state('withdrawal-transfer-failed.edit', {
            parent: 'withdrawal-transfer-failed',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/withdrawal-transfer-failed/withdrawal-transfer-failed-dialog.html',
                    controller: 'WithdrawalTransferFailedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WithdrawalTransferFailed', function(WithdrawalTransferFailed) {
                            return WithdrawalTransferFailed.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('withdrawal-transfer-failed', null, { reload: 'withdrawal-transfer-failed' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('withdrawal-transfer-failed.delete', {
            parent: 'withdrawal-transfer-failed',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/withdrawal-transfer-failed/withdrawal-transfer-failed-delete-dialog.html',
                    controller: 'WithdrawalTransferFailedDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WithdrawalTransferFailed', function(WithdrawalTransferFailed) {
                            return WithdrawalTransferFailed.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('withdrawal-transfer-failed', null, { reload: 'withdrawal-transfer-failed' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('wallet-audit', {
            parent: 'entity',
            url: '/wallet-audit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WalletAudits'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wallet-audit/wallet-audits.html',
                    controller: 'WalletAuditController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('wallet-audit-detail', {
            parent: 'wallet-audit',
            url: '/wallet-audit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WalletAudit'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/wallet-audit/wallet-audit-detail.html',
                    controller: 'WalletAuditDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WalletAudit', function($stateParams, WalletAudit) {
                    return WalletAudit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'wallet-audit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('wallet-audit-detail.edit', {
            parent: 'wallet-audit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-audit/wallet-audit-dialog.html',
                    controller: 'WalletAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WalletAudit', function(WalletAudit) {
                            return WalletAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wallet-audit.new', {
            parent: 'wallet-audit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-audit/wallet-audit-dialog.html',
                    controller: 'WalletAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                entityName: null,
                                entityId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('wallet-audit', null, { reload: 'wallet-audit' });
                }, function() {
                    $state.go('wallet-audit');
                });
            }]
        })
        .state('wallet-audit.edit', {
            parent: 'wallet-audit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-audit/wallet-audit-dialog.html',
                    controller: 'WalletAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WalletAudit', function(WalletAudit) {
                            return WalletAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wallet-audit', null, { reload: 'wallet-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('wallet-audit.delete', {
            parent: 'wallet-audit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/wallet-audit/wallet-audit-delete-dialog.html',
                    controller: 'WalletAuditDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WalletAudit', function(WalletAudit) {
                            return WalletAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('wallet-audit', null, { reload: 'wallet-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

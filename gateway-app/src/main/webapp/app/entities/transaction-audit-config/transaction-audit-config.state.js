(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transaction-audit-config', {
            parent: 'entity',
            url: '/transaction-audit-config',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransactionAuditConfigs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transaction-audit-config/transaction-audit-configs.html',
                    controller: 'TransactionAuditConfigController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('transaction-audit-config-detail', {
            parent: 'transaction-audit-config',
            url: '/transaction-audit-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransactionAuditConfig'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transaction-audit-config/transaction-audit-config-detail.html',
                    controller: 'TransactionAuditConfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TransactionAuditConfig', function($stateParams, TransactionAuditConfig) {
                    return TransactionAuditConfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transaction-audit-config',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transaction-audit-config-detail.edit', {
            parent: 'transaction-audit-config-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction-audit-config/transaction-audit-config-dialog.html',
                    controller: 'TransactionAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransactionAuditConfig', function(TransactionAuditConfig) {
                            return TransactionAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transaction-audit-config.new', {
            parent: 'transaction-audit-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction-audit-config/transaction-audit-config-dialog.html',
                    controller: 'TransactionAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                entityName: null,
                                activeStatus: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('transaction-audit-config', null, { reload: 'transaction-audit-config' });
                }, function() {
                    $state.go('transaction-audit-config');
                });
            }]
        })
        .state('transaction-audit-config.edit', {
            parent: 'transaction-audit-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction-audit-config/transaction-audit-config-dialog.html',
                    controller: 'TransactionAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransactionAuditConfig', function(TransactionAuditConfig) {
                            return TransactionAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaction-audit-config', null, { reload: 'transaction-audit-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transaction-audit-config.delete', {
            parent: 'transaction-audit-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction-audit-config/transaction-audit-config-delete-dialog.html',
                    controller: 'TransactionAuditConfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TransactionAuditConfig', function(TransactionAuditConfig) {
                            return TransactionAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaction-audit-config', null, { reload: 'transaction-audit-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transaction-audit', {
            parent: 'entity',
            url: '/transaction-audit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransactionAudits'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transaction-audit/transaction-audits.html',
                    controller: 'TransactionAuditController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('transaction-audit-detail', {
            parent: 'transaction-audit',
            url: '/transaction-audit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TransactionAudit'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transaction-audit/transaction-audit-detail.html',
                    controller: 'TransactionAuditDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TransactionAudit', function($stateParams, TransactionAudit) {
                    return TransactionAudit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transaction-audit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transaction-audit-detail.edit', {
            parent: 'transaction-audit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction-audit/transaction-audit-dialog.html',
                    controller: 'TransactionAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransactionAudit', function(TransactionAudit) {
                            return TransactionAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transaction-audit.new', {
            parent: 'transaction-audit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction-audit/transaction-audit-dialog.html',
                    controller: 'TransactionAuditDialogController',
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
                    $state.go('transaction-audit', null, { reload: 'transaction-audit' });
                }, function() {
                    $state.go('transaction-audit');
                });
            }]
        })
        .state('transaction-audit.edit', {
            parent: 'transaction-audit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction-audit/transaction-audit-dialog.html',
                    controller: 'TransactionAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransactionAudit', function(TransactionAudit) {
                            return TransactionAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaction-audit', null, { reload: 'transaction-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transaction-audit.delete', {
            parent: 'transaction-audit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transaction-audit/transaction-audit-delete-dialog.html',
                    controller: 'TransactionAuditDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TransactionAudit', function(TransactionAudit) {
                            return TransactionAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transaction-audit', null, { reload: 'transaction-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

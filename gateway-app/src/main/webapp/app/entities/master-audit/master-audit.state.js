(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('master-audit', {
            parent: 'entity',
            url: '/master-audit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MasterAudits'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/master-audit/master-audits.html',
                    controller: 'MasterAuditController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('master-audit-detail', {
            parent: 'master-audit',
            url: '/master-audit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MasterAudit'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/master-audit/master-audit-detail.html',
                    controller: 'MasterAuditDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MasterAudit', function($stateParams, MasterAudit) {
                    return MasterAudit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'master-audit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('master-audit-detail.edit', {
            parent: 'master-audit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/master-audit/master-audit-dialog.html',
                    controller: 'MasterAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MasterAudit', function(MasterAudit) {
                            return MasterAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('master-audit.new', {
            parent: 'master-audit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/master-audit/master-audit-dialog.html',
                    controller: 'MasterAuditDialogController',
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
                    $state.go('master-audit', null, { reload: 'master-audit' });
                }, function() {
                    $state.go('master-audit');
                });
            }]
        })
        .state('master-audit.edit', {
            parent: 'master-audit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/master-audit/master-audit-dialog.html',
                    controller: 'MasterAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MasterAudit', function(MasterAudit) {
                            return MasterAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('master-audit', null, { reload: 'master-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('master-audit.delete', {
            parent: 'master-audit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/master-audit/master-audit-delete-dialog.html',
                    controller: 'MasterAuditDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MasterAudit', function(MasterAudit) {
                            return MasterAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('master-audit', null, { reload: 'master-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

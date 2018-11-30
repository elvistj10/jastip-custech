(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('banner-audit', {
            parent: 'entity',
            url: '/banner-audit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BannerAudits'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/banner-audit/banner-audits.html',
                    controller: 'BannerAuditController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('banner-audit-detail', {
            parent: 'banner-audit',
            url: '/banner-audit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BannerAudit'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/banner-audit/banner-audit-detail.html',
                    controller: 'BannerAuditDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'BannerAudit', function($stateParams, BannerAudit) {
                    return BannerAudit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'banner-audit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('banner-audit-detail.edit', {
            parent: 'banner-audit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banner-audit/banner-audit-dialog.html',
                    controller: 'BannerAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BannerAudit', function(BannerAudit) {
                            return BannerAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('banner-audit.new', {
            parent: 'banner-audit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banner-audit/banner-audit-dialog.html',
                    controller: 'BannerAuditDialogController',
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
                    $state.go('banner-audit', null, { reload: 'banner-audit' });
                }, function() {
                    $state.go('banner-audit');
                });
            }]
        })
        .state('banner-audit.edit', {
            parent: 'banner-audit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banner-audit/banner-audit-dialog.html',
                    controller: 'BannerAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BannerAudit', function(BannerAudit) {
                            return BannerAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('banner-audit', null, { reload: 'banner-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('banner-audit.delete', {
            parent: 'banner-audit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banner-audit/banner-audit-delete-dialog.html',
                    controller: 'BannerAuditDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BannerAudit', function(BannerAudit) {
                            return BannerAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('banner-audit', null, { reload: 'banner-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

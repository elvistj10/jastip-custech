(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('profiles-audit', {
            parent: 'entity',
            url: '/profiles-audit',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProfilesAudits'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/profiles-audit/profiles-audits.html',
                    controller: 'ProfilesAuditController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('profiles-audit-detail', {
            parent: 'profiles-audit',
            url: '/profiles-audit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProfilesAudit'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/profiles-audit/profiles-audit-detail.html',
                    controller: 'ProfilesAuditDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ProfilesAudit', function($stateParams, ProfilesAudit) {
                    return ProfilesAudit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'profiles-audit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('profiles-audit-detail.edit', {
            parent: 'profiles-audit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profiles-audit/profiles-audit-dialog.html',
                    controller: 'ProfilesAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProfilesAudit', function(ProfilesAudit) {
                            return ProfilesAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('profiles-audit.new', {
            parent: 'profiles-audit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profiles-audit/profiles-audit-dialog.html',
                    controller: 'ProfilesAuditDialogController',
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
                    $state.go('profiles-audit', null, { reload: 'profiles-audit' });
                }, function() {
                    $state.go('profiles-audit');
                });
            }]
        })
        .state('profiles-audit.edit', {
            parent: 'profiles-audit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profiles-audit/profiles-audit-dialog.html',
                    controller: 'ProfilesAuditDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProfilesAudit', function(ProfilesAudit) {
                            return ProfilesAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('profiles-audit', null, { reload: 'profiles-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('profiles-audit.delete', {
            parent: 'profiles-audit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profiles-audit/profiles-audit-delete-dialog.html',
                    controller: 'ProfilesAuditDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProfilesAudit', function(ProfilesAudit) {
                            return ProfilesAudit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('profiles-audit', null, { reload: 'profiles-audit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

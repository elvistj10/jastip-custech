(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('profiles-audit-config', {
            parent: 'entity',
            url: '/profiles-audit-config',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProfilesAuditConfigs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/profiles-audit-config/profiles-audit-configs.html',
                    controller: 'ProfilesAuditConfigController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('profiles-audit-config-detail', {
            parent: 'profiles-audit-config',
            url: '/profiles-audit-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ProfilesAuditConfig'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/profiles-audit-config/profiles-audit-config-detail.html',
                    controller: 'ProfilesAuditConfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ProfilesAuditConfig', function($stateParams, ProfilesAuditConfig) {
                    return ProfilesAuditConfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'profiles-audit-config',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('profiles-audit-config-detail.edit', {
            parent: 'profiles-audit-config-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profiles-audit-config/profiles-audit-config-dialog.html',
                    controller: 'ProfilesAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProfilesAuditConfig', function(ProfilesAuditConfig) {
                            return ProfilesAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('profiles-audit-config.new', {
            parent: 'profiles-audit-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profiles-audit-config/profiles-audit-config-dialog.html',
                    controller: 'ProfilesAuditConfigDialogController',
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
                    $state.go('profiles-audit-config', null, { reload: 'profiles-audit-config' });
                }, function() {
                    $state.go('profiles-audit-config');
                });
            }]
        })
        .state('profiles-audit-config.edit', {
            parent: 'profiles-audit-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profiles-audit-config/profiles-audit-config-dialog.html',
                    controller: 'ProfilesAuditConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProfilesAuditConfig', function(ProfilesAuditConfig) {
                            return ProfilesAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('profiles-audit-config', null, { reload: 'profiles-audit-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('profiles-audit-config.delete', {
            parent: 'profiles-audit-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/profiles-audit-config/profiles-audit-config-delete-dialog.html',
                    controller: 'ProfilesAuditConfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProfilesAuditConfig', function(ProfilesAuditConfig) {
                            return ProfilesAuditConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('profiles-audit-config', null, { reload: 'profiles-audit-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

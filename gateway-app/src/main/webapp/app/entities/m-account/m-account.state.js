(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('m-account', {
            parent: 'entity',
            url: '/m-account',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MAccounts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-account/m-accounts.html',
                    controller: 'MAccountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('m-account-detail', {
            parent: 'm-account',
            url: '/m-account/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MAccount'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-account/m-account-detail.html',
                    controller: 'MAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MAccount', function($stateParams, MAccount) {
                    return MAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'm-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('m-account-detail.edit', {
            parent: 'm-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-account/m-account-dialog.html',
                    controller: 'MAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MAccount', function(MAccount) {
                            return MAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-account.new', {
            parent: 'm-account',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-account/m-account-dialog.html',
                    controller: 'MAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                coorperateId: null,
                                accountNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('m-account', null, { reload: 'm-account' });
                }, function() {
                    $state.go('m-account');
                });
            }]
        })
        .state('m-account.edit', {
            parent: 'm-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-account/m-account-dialog.html',
                    controller: 'MAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MAccount', function(MAccount) {
                            return MAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-account', null, { reload: 'm-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-account.delete', {
            parent: 'm-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-account/m-account-delete-dialog.html',
                    controller: 'MAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MAccount', function(MAccount) {
                            return MAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-account', null, { reload: 'm-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('business-account', {
            parent: 'entity',
            url: '/business-account',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BusinessAccounts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/business-account/business-accounts.html',
                    controller: 'BusinessAccountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('business-account-detail', {
            parent: 'business-account',
            url: '/business-account/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'BusinessAccount'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/business-account/business-account-detail.html',
                    controller: 'BusinessAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'BusinessAccount', function($stateParams, BusinessAccount) {
                    return BusinessAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'business-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('business-account-detail.edit', {
            parent: 'business-account-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business-account/business-account-dialog.html',
                    controller: 'BusinessAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BusinessAccount', function(BusinessAccount) {
                            return BusinessAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('business-account.new', {
            parent: 'business-account',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business-account/business-account-dialog.html',
                    controller: 'BusinessAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                coorperateId: null,
                                accountNumber: null,
                                bankName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('business-account', null, { reload: 'business-account' });
                }, function() {
                    $state.go('business-account');
                });
            }]
        })
        .state('business-account.edit', {
            parent: 'business-account',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business-account/business-account-dialog.html',
                    controller: 'BusinessAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BusinessAccount', function(BusinessAccount) {
                            return BusinessAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('business-account', null, { reload: 'business-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('business-account.delete', {
            parent: 'business-account',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/business-account/business-account-delete-dialog.html',
                    controller: 'BusinessAccountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BusinessAccount', function(BusinessAccount) {
                            return BusinessAccount.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('business-account', null, { reload: 'business-account' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

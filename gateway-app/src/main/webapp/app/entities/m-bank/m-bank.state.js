(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('m-bank', {
            parent: 'entity',
            url: '/m-bank',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MBanks'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-bank/m-banks.html',
                    controller: 'MBankController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('m-bank-detail', {
            parent: 'm-bank',
            url: '/m-bank/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MBank'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-bank/m-bank-detail.html',
                    controller: 'MBankDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MBank', function($stateParams, MBank) {
                    return MBank.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'm-bank',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('m-bank-detail.edit', {
            parent: 'm-bank-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-bank/m-bank-dialog.html',
                    controller: 'MBankDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MBank', function(MBank) {
                            return MBank.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-bank.new', {
            parent: 'm-bank',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-bank/m-bank-dialog.html',
                    controller: 'MBankDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                bankName: null,
                                activeStatus: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('m-bank', null, { reload: 'm-bank' });
                }, function() {
                    $state.go('m-bank');
                });
            }]
        })
        .state('m-bank.edit', {
            parent: 'm-bank',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-bank/m-bank-dialog.html',
                    controller: 'MBankDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MBank', function(MBank) {
                            return MBank.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-bank', null, { reload: 'm-bank' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-bank.delete', {
            parent: 'm-bank',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-bank/m-bank-delete-dialog.html',
                    controller: 'MBankDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MBank', function(MBank) {
                            return MBank.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-bank', null, { reload: 'm-bank' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

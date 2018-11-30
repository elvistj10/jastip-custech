(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bank', {
            parent: 'entity',
            url: '/bank',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Banks'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bank/banks.html',
                    controller: 'BankController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('bank-detail', {
            parent: 'bank',
            url: '/bank/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Bank'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bank/bank-detail.html',
                    controller: 'BankDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Bank', function($stateParams, Bank) {
                    return Bank.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bank',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bank-detail.edit', {
            parent: 'bank-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bank/bank-dialog.html',
                    controller: 'BankDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bank', function(Bank) {
                            return Bank.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bank.new', {
            parent: 'bank',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bank/bank-dialog.html',
                    controller: 'BankDialogController',
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
                    $state.go('bank', null, { reload: 'bank' });
                }, function() {
                    $state.go('bank');
                });
            }]
        })
        .state('bank.edit', {
            parent: 'bank',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bank/bank-dialog.html',
                    controller: 'BankDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bank', function(Bank) {
                            return Bank.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bank', null, { reload: 'bank' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bank.delete', {
            parent: 'bank',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bank/bank-delete-dialog.html',
                    controller: 'BankDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bank', function(Bank) {
                            return Bank.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bank', null, { reload: 'bank' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('withdrawal-transfer-list', {
            parent: 'entity',
            url: '/withdrawal-transfer-list',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WithdrawalTransferLists'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/withdrawal-transfer-list/withdrawal-transfer-lists.html',
                    controller: 'WithdrawalTransferListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('withdrawal-transfer-list-detail', {
            parent: 'withdrawal-transfer-list',
            url: '/withdrawal-transfer-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'WithdrawalTransferList'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/withdrawal-transfer-list/withdrawal-transfer-list-detail.html',
                    controller: 'WithdrawalTransferListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'WithdrawalTransferList', function($stateParams, WithdrawalTransferList) {
                    return WithdrawalTransferList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'withdrawal-transfer-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('withdrawal-transfer-list-detail.edit', {
            parent: 'withdrawal-transfer-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/withdrawal-transfer-list/withdrawal-transfer-list-dialog.html',
                    controller: 'WithdrawalTransferListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WithdrawalTransferList', function(WithdrawalTransferList) {
                            return WithdrawalTransferList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('withdrawal-transfer-list.new', {
            parent: 'withdrawal-transfer-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/withdrawal-transfer-list/withdrawal-transfer-list-dialog.html',
                    controller: 'WithdrawalTransferListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                withdrawalId: null,
                                nominal: null,
                                destBankName: null,
                                destBankAccount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('withdrawal-transfer-list', null, { reload: 'withdrawal-transfer-list' });
                }, function() {
                    $state.go('withdrawal-transfer-list');
                });
            }]
        })
        .state('withdrawal-transfer-list.edit', {
            parent: 'withdrawal-transfer-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/withdrawal-transfer-list/withdrawal-transfer-list-dialog.html',
                    controller: 'WithdrawalTransferListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WithdrawalTransferList', function(WithdrawalTransferList) {
                            return WithdrawalTransferList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('withdrawal-transfer-list', null, { reload: 'withdrawal-transfer-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('withdrawal-transfer-list.delete', {
            parent: 'withdrawal-transfer-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/withdrawal-transfer-list/withdrawal-transfer-list-delete-dialog.html',
                    controller: 'WithdrawalTransferListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WithdrawalTransferList', function(WithdrawalTransferList) {
                            return WithdrawalTransferList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('withdrawal-transfer-list', null, { reload: 'withdrawal-transfer-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

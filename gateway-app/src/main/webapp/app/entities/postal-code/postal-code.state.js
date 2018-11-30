(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('postal-code', {
            parent: 'entity',
            url: '/postal-code',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PostalCodes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/postal-code/postal-codes.html',
                    controller: 'PostalCodeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('postal-code-detail', {
            parent: 'postal-code',
            url: '/postal-code/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PostalCode'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/postal-code/postal-code-detail.html',
                    controller: 'PostalCodeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PostalCode', function($stateParams, PostalCode) {
                    return PostalCode.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'postal-code',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('postal-code-detail.edit', {
            parent: 'postal-code-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/postal-code/postal-code-dialog.html',
                    controller: 'PostalCodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PostalCode', function(PostalCode) {
                            return PostalCode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('postal-code.new', {
            parent: 'postal-code',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/postal-code/postal-code-dialog.html',
                    controller: 'PostalCodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                postalCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('postal-code', null, { reload: 'postal-code' });
                }, function() {
                    $state.go('postal-code');
                });
            }]
        })
        .state('postal-code.edit', {
            parent: 'postal-code',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/postal-code/postal-code-dialog.html',
                    controller: 'PostalCodeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PostalCode', function(PostalCode) {
                            return PostalCode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('postal-code', null, { reload: 'postal-code' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('postal-code.delete', {
            parent: 'postal-code',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/postal-code/postal-code-delete-dialog.html',
                    controller: 'PostalCodeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PostalCode', function(PostalCode) {
                            return PostalCode.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('postal-code', null, { reload: 'postal-code' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('m-package-size', {
            parent: 'entity',
            url: '/m-package-size',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MPackageSizes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-package-size/m-package-sizes.html',
                    controller: 'MPackageSizeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('m-package-size-detail', {
            parent: 'm-package-size',
            url: '/m-package-size/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MPackageSize'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-package-size/m-package-size-detail.html',
                    controller: 'MPackageSizeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MPackageSize', function($stateParams, MPackageSize) {
                    return MPackageSize.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'm-package-size',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('m-package-size-detail.edit', {
            parent: 'm-package-size-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-package-size/m-package-size-dialog.html',
                    controller: 'MPackageSizeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MPackageSize', function(MPackageSize) {
                            return MPackageSize.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-package-size.new', {
            parent: 'm-package-size',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-package-size/m-package-size-dialog.html',
                    controller: 'MPackageSizeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                packageSizeName: null,
                                packageSizeDesciption: null,
                                packageSizeThumb: null,
                                packageSizeThumbContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('m-package-size', null, { reload: 'm-package-size' });
                }, function() {
                    $state.go('m-package-size');
                });
            }]
        })
        .state('m-package-size.edit', {
            parent: 'm-package-size',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-package-size/m-package-size-dialog.html',
                    controller: 'MPackageSizeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MPackageSize', function(MPackageSize) {
                            return MPackageSize.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-package-size', null, { reload: 'm-package-size' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-package-size.delete', {
            parent: 'm-package-size',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-package-size/m-package-size-delete-dialog.html',
                    controller: 'MPackageSizeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MPackageSize', function(MPackageSize) {
                            return MPackageSize.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-package-size', null, { reload: 'm-package-size' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

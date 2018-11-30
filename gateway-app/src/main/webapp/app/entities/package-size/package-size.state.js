(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('package-size', {
            parent: 'entity',
            url: '/package-size',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PackageSizes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/package-size/package-sizes.html',
                    controller: 'PackageSizeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('package-size-detail', {
            parent: 'package-size',
            url: '/package-size/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PackageSize'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/package-size/package-size-detail.html',
                    controller: 'PackageSizeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PackageSize', function($stateParams, PackageSize) {
                    return PackageSize.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'package-size',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('package-size-detail.edit', {
            parent: 'package-size-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-size/package-size-dialog.html',
                    controller: 'PackageSizeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PackageSize', function(PackageSize) {
                            return PackageSize.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('package-size.new', {
            parent: 'package-size',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-size/package-size-dialog.html',
                    controller: 'PackageSizeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                packageSizeName: null,
                                packageSizeDesciption: null,
                                packageSizeIcon: null,
                                packageSizeIconContentType: null,
                                packageSizeIconUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('package-size', null, { reload: 'package-size' });
                }, function() {
                    $state.go('package-size');
                });
            }]
        })
        .state('package-size.edit', {
            parent: 'package-size',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-size/package-size-dialog.html',
                    controller: 'PackageSizeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PackageSize', function(PackageSize) {
                            return PackageSize.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('package-size', null, { reload: 'package-size' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('package-size.delete', {
            parent: 'package-size',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-size/package-size-delete-dialog.html',
                    controller: 'PackageSizeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PackageSize', function(PackageSize) {
                            return PackageSize.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('package-size', null, { reload: 'package-size' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

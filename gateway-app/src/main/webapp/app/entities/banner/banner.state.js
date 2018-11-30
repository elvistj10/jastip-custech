(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('banner', {
            parent: 'entity',
            url: '/banner',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Banners'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/banner/banners.html',
                    controller: 'BannerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('banner-detail', {
            parent: 'banner',
            url: '/banner/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Banner'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/banner/banner-detail.html',
                    controller: 'BannerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Banner', function($stateParams, Banner) {
                    return Banner.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'banner',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('banner-detail.edit', {
            parent: 'banner-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banner/banner-dialog.html',
                    controller: 'BannerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Banner', function(Banner) {
                            return Banner.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('banner.new', {
            parent: 'banner',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banner/banner-dialog.html',
                    controller: 'BannerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                bannerTitle: null,
                                bannerType: null,
                                postingType: null,
                                image: null,
                                imageContentType: null,
                                imageUrl: null,
                                startDate: null,
                                endDate: null,
                                description: null,
                                notes: null,
                                query: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('banner', null, { reload: 'banner' });
                }, function() {
                    $state.go('banner');
                });
            }]
        })
        .state('banner.edit', {
            parent: 'banner',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banner/banner-dialog.html',
                    controller: 'BannerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Banner', function(Banner) {
                            return Banner.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('banner', null, { reload: 'banner' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('banner.delete', {
            parent: 'banner',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/banner/banner-delete-dialog.html',
                    controller: 'BannerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Banner', function(Banner) {
                            return Banner.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('banner', null, { reload: 'banner' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

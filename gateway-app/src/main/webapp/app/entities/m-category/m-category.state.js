(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('m-category', {
            parent: 'entity',
            url: '/m-category',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MCategories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-category/m-categories.html',
                    controller: 'MCategoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('m-category-detail', {
            parent: 'm-category',
            url: '/m-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MCategory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-category/m-category-detail.html',
                    controller: 'MCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MCategory', function($stateParams, MCategory) {
                    return MCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'm-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('m-category-detail.edit', {
            parent: 'm-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-category/m-category-dialog.html',
                    controller: 'MCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MCategory', function(MCategory) {
                            return MCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-category.new', {
            parent: 'm-category',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-category/m-category-dialog.html',
                    controller: 'MCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                categoryName: null,
                                categoryIcon: null,
                                categoryIconContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('m-category', null, { reload: 'm-category' });
                }, function() {
                    $state.go('m-category');
                });
            }]
        })
        .state('m-category.edit', {
            parent: 'm-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-category/m-category-dialog.html',
                    controller: 'MCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MCategory', function(MCategory) {
                            return MCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-category', null, { reload: 'm-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-category.delete', {
            parent: 'm-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-category/m-category-delete-dialog.html',
                    controller: 'MCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MCategory', function(MCategory) {
                            return MCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-category', null, { reload: 'm-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

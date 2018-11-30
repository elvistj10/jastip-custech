(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('m-sub-category', {
            parent: 'entity',
            url: '/m-sub-category',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MSubCategories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-sub-category/m-sub-categories.html',
                    controller: 'MSubCategoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('m-sub-category-detail', {
            parent: 'm-sub-category',
            url: '/m-sub-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MSubCategory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/m-sub-category/m-sub-category-detail.html',
                    controller: 'MSubCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MSubCategory', function($stateParams, MSubCategory) {
                    return MSubCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'm-sub-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('m-sub-category-detail.edit', {
            parent: 'm-sub-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-sub-category/m-sub-category-dialog.html',
                    controller: 'MSubCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MSubCategory', function(MSubCategory) {
                            return MSubCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-sub-category.new', {
            parent: 'm-sub-category',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-sub-category/m-sub-category-dialog.html',
                    controller: 'MSubCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                subCategoryName: null,
                                subCategoryIcon: null,
                                subCategoryIconContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('m-sub-category', null, { reload: 'm-sub-category' });
                }, function() {
                    $state.go('m-sub-category');
                });
            }]
        })
        .state('m-sub-category.edit', {
            parent: 'm-sub-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-sub-category/m-sub-category-dialog.html',
                    controller: 'MSubCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MSubCategory', function(MSubCategory) {
                            return MSubCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-sub-category', null, { reload: 'm-sub-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('m-sub-category.delete', {
            parent: 'm-sub-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/m-sub-category/m-sub-category-delete-dialog.html',
                    controller: 'MSubCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MSubCategory', function(MSubCategory) {
                            return MSubCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('m-sub-category', null, { reload: 'm-sub-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

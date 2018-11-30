(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('item-sub-category', {
            parent: 'entity',
            url: '/item-sub-category',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ItemSubCategories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/item-sub-category/item-sub-categories.html',
                    controller: 'ItemSubCategoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('item-sub-category-detail', {
            parent: 'item-sub-category',
            url: '/item-sub-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ItemSubCategory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/item-sub-category/item-sub-category-detail.html',
                    controller: 'ItemSubCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ItemSubCategory', function($stateParams, ItemSubCategory) {
                    return ItemSubCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'item-sub-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('item-sub-category-detail.edit', {
            parent: 'item-sub-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-sub-category/item-sub-category-dialog.html',
                    controller: 'ItemSubCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ItemSubCategory', function(ItemSubCategory) {
                            return ItemSubCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('item-sub-category.new', {
            parent: 'item-sub-category',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-sub-category/item-sub-category-dialog.html',
                    controller: 'ItemSubCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                itemSubCategoryName: null,
                                itemSubCategoryIcon: null,
                                itemSubCategoryIconContentType: null,
                                itemSubCategoryIconUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('item-sub-category', null, { reload: 'item-sub-category' });
                }, function() {
                    $state.go('item-sub-category');
                });
            }]
        })
        .state('item-sub-category.edit', {
            parent: 'item-sub-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-sub-category/item-sub-category-dialog.html',
                    controller: 'ItemSubCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ItemSubCategory', function(ItemSubCategory) {
                            return ItemSubCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('item-sub-category', null, { reload: 'item-sub-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('item-sub-category.delete', {
            parent: 'item-sub-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-sub-category/item-sub-category-delete-dialog.html',
                    controller: 'ItemSubCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ItemSubCategory', function(ItemSubCategory) {
                            return ItemSubCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('item-sub-category', null, { reload: 'item-sub-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

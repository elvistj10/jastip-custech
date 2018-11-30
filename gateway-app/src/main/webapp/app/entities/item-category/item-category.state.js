(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('item-category', {
            parent: 'entity',
            url: '/item-category',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ItemCategories'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/item-category/item-categories.html',
                    controller: 'ItemCategoryController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('item-category-detail', {
            parent: 'item-category',
            url: '/item-category/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ItemCategory'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/item-category/item-category-detail.html',
                    controller: 'ItemCategoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ItemCategory', function($stateParams, ItemCategory) {
                    return ItemCategory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'item-category',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('item-category-detail.edit', {
            parent: 'item-category-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-category/item-category-dialog.html',
                    controller: 'ItemCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ItemCategory', function(ItemCategory) {
                            return ItemCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('item-category.new', {
            parent: 'item-category',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-category/item-category-dialog.html',
                    controller: 'ItemCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                itemCategoryName: null,
                                itemCategoryIcon: null,
                                itemCategoryIconContentType: null,
                                itemCategoryIconUrl: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('item-category', null, { reload: 'item-category' });
                }, function() {
                    $state.go('item-category');
                });
            }]
        })
        .state('item-category.edit', {
            parent: 'item-category',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-category/item-category-dialog.html',
                    controller: 'ItemCategoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ItemCategory', function(ItemCategory) {
                            return ItemCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('item-category', null, { reload: 'item-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('item-category.delete', {
            parent: 'item-category',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/item-category/item-category-delete-dialog.html',
                    controller: 'ItemCategoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ItemCategory', function(ItemCategory) {
                            return ItemCategory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('item-category', null, { reload: 'item-category' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

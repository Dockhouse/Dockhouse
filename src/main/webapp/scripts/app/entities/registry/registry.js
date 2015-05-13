/*
 *  Copyright (C) 2015  Dockhouse project org. ( http://dockhouse.github.io/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

'use strict';

angular.module('dockhouseApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('registry', {
                parent: 'entity',
                url: '/registry',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dockhouseApp.registry.home.title',
                    displayName: 'dockhouseApp.registry.home.route'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/registry/registries.html',
                        controller: 'RegistryController as registryCtrl'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('registry');
                        return $translate.refresh();
                    }]
                }
            })
            .state('registryDetail', {
                parent: 'registry',
                url: '/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dockhouseApp.registry.detail.title',
                    displayName: 'dockhouseApp.registry.detail.route'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/registry/registry-detail.html',
                        controller: 'RegistryDetailController as detailCtrl'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('registry');
                        return $translate.refresh();
                    }]
                }
            })
            .state('imageDetail', {
                parent: 'registryDetail',
                url: '/images/:imageID',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dockhouseApp.images.home.title',
                    displayName: 'dockhouseApp.images.home.route'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/registry/image-detail.html',
                        controller: 'ImageDetailController as imageCtrl'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('image');
                        return $translate.refresh();
                    }]
                }
            });
    });

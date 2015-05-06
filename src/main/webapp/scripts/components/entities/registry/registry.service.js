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
(function (){
    "use strict";

    angular
        .module('dockhouseApp')
        .factory('Registry', Registry);

    Registry.$inject = ['Restangular', 'Logger', '$q'];

    /* @ngInject */
    function Registry(Restangular, Logger, $q){
        var logger = Logger.getInstance('RegistryService');

        var service = Restangular.service('registries');


        return {
            get: get,
            getAll: getAll,
            create: create,
            update: update,
            remove: remove,
            testRegistry : testRegistry,
            getDetail : getDetail,
            getAllImages : getAllImages,
            deleteImage : deleteImage,
            getImageTags : getImageTags,
            testImage : testImage
        };

        ////////////////

        /**
         * Get a registry from an id
         * @returns the registry corresponding (promise){*}
         */
        function get(id) {
            logger.debug('GET /registries/'+id);
            return service.one(id)
                .get()
                .then(function(data) {
                    return data;
                })
                .catch(function(error) {
                    logger.error('registries/:id',"Error lors de l'appel du service REST registries",error);
                    throw error;
                });
        }

        /**
         * Get the list of all the registries
         * @returns the complete array of registries (promise){*}
         */
        function getAll() {
            logger.debug('GET /registries');
            return service
                .getList()
                .then(function(data) {
                    return data;
                })
                .catch(function(error) {
                    logger.error('registries',"Error lors de l'appel du service REST registries",error);
                    throw error;
                });
        }


        /**
         * Create a new registry
         * @param registry to create
         * @returns the registry inserted
         */
        function create(registry) {
            logger.debug('POST /registries');
            return service
                .post(registry)
                .then(function(data) {
                    return data;
                })
                .catch(function (error){
                    logger.error('registries',"Error lors de l'appel du service REST registries",error);
                    throw error;
                });
        }

        /**
         * Update registry
         * @param registry to update
         * @returns the registry updated
         */
        function update(registry) {
            logger.debug('PUT /registries');
            return registry
                .put()
                .then(function(data) {
                    return data;
                })
                .catch(function (error) {
                    logger.error('registries',"Error lors de l'appel du service REST registries",error);
                    throw error;
                });
        }

        /**
         * Remove registry
         * @param registry to remove
         * @returns the registry removed
         */
        function remove(registry) {
            logger.debug('DELETE /registries');
            return registry
                .remove()
                .then(function(data) {
                    return data;
                })
                .catch(function (error) {
                    logger.error('registries',"Error lors de l'appel du service REST registries",error);
                    throw error;
                });
        }

        /**
         * Test the registry
         * @param id - the ID of the given registry
         * @returns online/offline depending on the registry status.
         */
        function testRegistry(id) {
            return service.one(id).customGET("status")
                .then(function(data) {
                    return data.status;
                })
                .catch(function(error) {
                    logger.error('registries/:id/status',"Error lors de l'appel du service REST registries status",error);
                    throw error;
                });
        }

        /**
         * Get details of a registry from its ID.
         * @param registryId - the registry ID.
         * @returns a JSON object representing the registry details.
         */
        function getDetail(registryId) {
            return service.one(registryId).customGET("details")
                .then(function(data) {
                    return data.details;
                })
                .catch(function(error) {
                    logger.error('registries/:id/details',"Error lors de l'appel du service REST registries info",error);
                    throw error;
                });
        }

        /**
         * Get all the images contained in a registry.
         * @param registryId - The registry ID.
         * @returns a JSON array with the image list.
         */
        function getAllImages(registryId) {
            return service.one(registryId).customGET("images")
                .then(function (data) {
                    return data;
                })
                .catch(function (error) {
                    logger.error('registries/:id/images', "Error lors de l'appel du service REST registries images", error);
                    throw error;
                });
        }


        /**
         * Delete an image contained in a registry.
         * @param registryId - The registry ID.
         * @param imageId - The image name
         * @returns a JSON indicating if the image has been deleted
         */
        function deleteImage(registryId, imageId) {
            return service.one(registryId).one('images').one(imageId).remove()
                .then(function(data) {
                    return data;
                })
                .catch(function(error) {
                    logger.error('registries/'+registryId+'/images/'+imageId+'',"Error lors de l'appel du service REST registries images",error);
                    throw error;
                });
        }

        /**
         * Gets the tags of an image contained in a registry.
         * @param registryId - The registry ID.
         * @param imageId - The image name
         * @returns JSON object representing the image tags.
         */
        function getImageTags(registryId, imageId){
            return service.one(registryId).one('images').one(imageId).customGET('tags')
                .then(function(data){
                    return data.tags;
                })
                .catch(function(error){
                    logger.error('registries/'+registryId+'/images/'+imageId+'/tags',"Error lors de l'appel du service REST registries images", error);
                    throw error;
                });
        }

        /**
         * Test the image to see if it is contained in the registry
         * @param registryId - The registry ID.
         * @param imageId - The image name
         * @returns online/offline depending on if the image is found in the registry.
         */
        function testImage(registryId, imageId) {
            return service.one(registryId).one('images').one(imageId).customGET('status')
                .then(function(data) {
                    return data.status;
                })
                .catch(function(error) {
                    logger.error('registries/'+registryId+'/images/'+imageId+'/status',"Error lors de l'appel du service REST registries image status",error);
                    throw error;
                });
        }
    }

})();


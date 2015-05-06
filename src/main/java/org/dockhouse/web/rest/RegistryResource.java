/**
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
package org.dockhouse.web.rest;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.dockhouse.domain.Registry;
import org.dockhouse.repository.RegistryRepository;
import org.dockhouse.service.RegistryService;
import org.dockhouse.web.rest.dto.RegistryDeleteImageDTO;
import org.dockhouse.web.rest.dto.RegistryDetailsDTO;
import org.dockhouse.web.rest.dto.RegistryImageDTO;
import org.dockhouse.web.rest.dto.RegistryImageStatusDTO;
import org.dockhouse.web.rest.dto.RegistryImageTagsDTO;
import org.dockhouse.web.rest.dto.RegistryStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing registries.
 */
@RestController
@RequestMapping("/api")
public class RegistryResource {

    private final Logger log = LoggerFactory.getLogger(RegistryResource.class);

    @Inject
    private RegistryRepository registryRepository;

    @Inject
    private RegistryService registryService;

    @Inject
    private Validator validator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }
    
    /**
     * GET  /registries -> get all registries.
     */
    @RequestMapping(value = "/registries",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<List<Registry>> getRegistries() {
        log.debug("REST request to get all Registries");
        return new ResponseEntity<>(registryService.getAll(), 
        						    HttpStatus.OK);
    }

    /**
     * GET  /registries/:id -> get the "id" registry.
     */
    @RequestMapping(value = "/registries/{id}",
		    method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<Registry> getRegistry(@PathVariable String id) {
        log.debug("REST request to get Registry : {}", id);
        return registryService.getOne(id)
            .map(registry -> new ResponseEntity<>(registry, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /registries/:id/status -> get the "id" registry status.
     */
    @RequestMapping(value = "/registries/{id}/status",
		    method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<RegistryStatusDTO> getRegistryStatus(@PathVariable String id) {
        log.debug("REST request to get Registry status : {}", id);
        RegistryStatusDTO status = registryService.getStatus(id);
        return new ResponseEntity<RegistryStatusDTO>(status, HttpStatus.OK);
    }
    
    /**
     * GET  /registries/:id/details -> get the "id" registry details.
     */
    @RequestMapping(value = "/registries/{id}/details",
		    method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<RegistryDetailsDTO> getRegistryDetails(@PathVariable String id) {
        log.debug("REST request to get Registry details : {}", id);
        RegistryDetailsDTO details = registryService.getDetails(id);
        return new ResponseEntity<RegistryDetailsDTO>(details, HttpStatus.OK);
    }
    
    /**
     * GET  /registries/:id/images -> get the "id" registry images.
     */
    @RequestMapping(value = "/registries/{id}/images",
		    method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<List<RegistryImageDTO>> getRegistryImages(@PathVariable String id) {
        log.debug("REST request to get Registry images : {}", id);
        List<RegistryImageDTO> images = registryService.getImages(id);
        return new ResponseEntity<List<RegistryImageDTO>>(images, HttpStatus.OK);
    }
    
    /**
     * DELETE  /registries/:id/images/:namespace/:repository -> delete the image with imageName
     * from the "id" registry images.
     */
    @RequestMapping(value = "/registries/{id}/images/{namespace}/{repository}",
		    method = RequestMethod.DELETE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<RegistryDeleteImageDTO> deleteImageFromRegistry(@PathVariable String id,
    		@PathVariable String namespace, @PathVariable String repository) {
    	String imageName = namespace+"/"+repository;
        log.debug("REST request to delete the image from the Registry : {}", id + " : " + imageName);
        RegistryDeleteImageDTO deleteImageResponse = registryService.deleteImage(id, imageName);
        if(deleteImageResponse.isDeleted()){
            return new ResponseEntity<RegistryDeleteImageDTO>(deleteImageResponse, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<RegistryDeleteImageDTO>(deleteImageResponse, HttpStatus.METHOD_NOT_ALLOWED);
        }
    }
    
    
    /**
     * GET  /registries/:id/images/:namespace/:repository/tags -> get the tags of the image in the
     * "id" registry.
     */
    @RequestMapping(value = "/registries/{id}/images/{namespace}/{repository}/tags",
		    method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<RegistryImageTagsDTO> getRegistryImageTags(@PathVariable String id,
    		@PathVariable String namespace, @PathVariable String repository) {
    	String imageName = namespace+"/"+repository;
        log.debug("REST request to get tags from the image in the Registry : {}", id+" : "+imageName);
        RegistryImageTagsDTO tags = registryService.getImageTags(id, imageName);
        return new ResponseEntity<RegistryImageTagsDTO>(tags, HttpStatus.OK);
    }
    
        
    /**
     * POST  /registries -> Create a new registry.
     */
    @RequestMapping(value = "/registries",
		    method = RequestMethod.POST,
		    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Registry> createRegistry(@RequestBody @Valid Registry registry) {
        log.debug("REST request to save Registry : {}", registry);        	
        registry = registryService.insert(registry);
        return new ResponseEntity<Registry>(registry, HttpStatus.CREATED);
    }

    /**
     * PUT  /registries/:id -> update an registry.
     */
    @RequestMapping(value = "/registries/{id}",
		    method = RequestMethod.PUT,
		    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Registry> updateRegistry(@RequestBody @Valid Registry registry,
    						                       @PathVariable String id) {
        log.debug("REST request to save Registry : {}", registry);        	
        registry = registryService.upsert(registry, id);
        return new ResponseEntity<Registry>(registry, HttpStatus.OK);
    }

    /**
     * DELETE  /registries/:id -> delete the "id" registry.
     */
    @RequestMapping(value = "/registries/{id}",
		    method = RequestMethod.DELETE,
		    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Timed
    public void deleteRegistry(@PathVariable String id) {
        log.debug("REST request to delete Registry : {}", id);
        registryRepository.delete(id);
    }
    
    /**
     * GET /registries/:id/images/:namespace/:repository/status -> get the "id" registry status.
     */
    @RequestMapping(value = "/registries/{id}/images/{namespace}/{repository}/status",
		    method = RequestMethod.GET,
		    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<RegistryImageStatusDTO> getImageStatus(@PathVariable String id,
    		@PathVariable String namespace, @PathVariable String repository) {
    	String imageName = namespace + "/" + repository;
        log.debug("REST request to get status from the image in the Registry : {}", id+" : "+imageName);
        RegistryImageStatusDTO status = registryService.getImageStatus(id, imageName);
        return new ResponseEntity<RegistryImageStatusDTO>(status, HttpStatus.OK);
    }
}
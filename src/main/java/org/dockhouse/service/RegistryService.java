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
package org.dockhouse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.dockhouse.domain.Registry;
import org.dockhouse.repository.RegistryRepository;
import org.dockhouse.service.registryapi.RegistryAPIService;
import org.dockhouse.service.registryapi.RegistryAPIServiceFactory;
import org.dockhouse.web.rest.dto.RegistryDeleteImageDTO;
import org.dockhouse.web.rest.dto.RegistryDetailsDTO;
import org.dockhouse.web.rest.dto.RegistryImageDTO;
import org.dockhouse.web.rest.dto.RegistryImageStatusDTO;
import org.dockhouse.web.rest.dto.RegistryImageTagsDTO;
import org.dockhouse.web.rest.dto.RegistryStatusDTO;
import org.dockhouse.web.rest.dto.StatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service class for managing registries.
 */
@Service
public class RegistryService {

	private final Logger log = LoggerFactory.getLogger(RegistryService.class);

	@Inject
	private RegistryRepository registryRepository;

	@Inject
	private RegistryAPIServiceFactory registryAPIServiceFactory;

	public List<Registry> getAll() {
		return registryRepository.findAll()
				.stream()
				.collect(Collectors.toList());
	}

	public Optional<Registry> getOne(String id) {
		return Optional.ofNullable(registryRepository.findOne(id));
	}

	public Registry insert(Registry registry) {
		registry.setId(null);
		return registryRepository.save(registry);
	}

	public Registry upsert(Registry registry, String id) {
		registry.setId(id);
		return registryRepository.save(registry);
	}

	public RegistryStatusDTO getStatus(String id){
		RegistryStatusDTO registryStatusDTO = new RegistryStatusDTO();
		registryStatusDTO.setStatus(RegistryStatusDTO.STATUT_OFFLINE);

		Registry registry = registryRepository.findOne(id);
		if(registry != null){
			try{
				RegistryAPIService registreAPI = registryAPIServiceFactory.get(registry);
				if(registreAPI.isAvailable(registry)){
					registryStatusDTO.setStatus(RegistryStatusDTO.STATUT_ONLINE);
				}
			}
			catch(IllegalArgumentException e){
				log.debug(e.getMessage());
			}            
		}

		return registryStatusDTO;
	}

	public RegistryDetailsDTO getDetails(String id){
		RegistryDetailsDTO registryDetailsDTO = new RegistryDetailsDTO();
		registryDetailsDTO.setDetails(new String());

		Registry registry = registryRepository.findOne(id);
		if(registry != null){
			try{
				RegistryAPIService registreAPI = registryAPIServiceFactory.get(registry);
				registryDetailsDTO.setDetails(registreAPI.getDetails(registry));
			}
			catch(IllegalArgumentException e){
				log.debug(e.getMessage());
			}
		}
		return registryDetailsDTO;
	}
	
	public List<RegistryImageDTO> getImages(String id){
		List<RegistryImageDTO> registryImagesList = new ArrayList<RegistryImageDTO>();
		
		Registry registry = registryRepository.findOne(id);
		if(registry != null){
			try{
				RegistryAPIService registreAPI = registryAPIServiceFactory.get(registry);
				registryImagesList = registreAPI.getImages(registry);
			}
			catch(IllegalArgumentException e){
				log.debug(e.getMessage());
			}
		}
		return registryImagesList;
	}
	
	public RegistryDeleteImageDTO deleteImage(String id, String imageName){
		RegistryDeleteImageDTO deleteResponse = new RegistryDeleteImageDTO();
		deleteResponse.setDeleted(false);
		
		Registry registry = registryRepository.findOne(id);
		if(registry != null){
			try{
				RegistryAPIService registreAPI = registryAPIServiceFactory.get(registry);
				deleteResponse.setDeleted(registreAPI.deleteImage(registry, imageName));
			}
			catch(IllegalArgumentException e){
				log.debug(e.getMessage());
			}
		}
		return deleteResponse;
	}
	
	public RegistryImageTagsDTO getImageTags(String id, String imageName){
		RegistryImageTagsDTO tags = new RegistryImageTagsDTO();
		tags.setTags(new String());
		
		Registry registry = registryRepository.findOne(id);
		if(registry != null){
			try{
				RegistryAPIService registreAPI = registryAPIServiceFactory.get(registry);
				tags.setTags(registreAPI.getImageTags(registry, imageName));
			}
			catch(IllegalArgumentException e){
				log.debug(e.getMessage());
			}
		}
		return tags;
	}
	
	public RegistryImageStatusDTO getImageStatus(String id, String imageName){
		RegistryImageStatusDTO status = new RegistryImageStatusDTO();
		status.setStatus(RegistryImageStatusDTO.STATUT_OFFLINE);
		
		Registry registry = registryRepository.findOne(id);
		if(registry != null){
			try{
				RegistryAPIService registreAPI = registryAPIServiceFactory.get(registry);
				List<RegistryImageDTO> registryImagesList = new ArrayList<RegistryImageDTO>();
				registryImagesList = registreAPI.getImages(registry);
				if(imageExistInListImages(imageName, registryImagesList)){
					status.setStatus(RegistryImageStatusDTO.STATUT_ONLINE);
				}
			}
			catch(IllegalArgumentException e){
				log.debug(e.getMessage());
			}
		}
		
		return status;
	}
	
	private boolean imageExistInListImages(String imageName, List<RegistryImageDTO> list){
		boolean ret = false;
		for(int i=0; i < list.size(); i++){
			if(imageName.equals(list.get(i).getName())){
				ret = true;
				break;
			}
		}
		return ret;
	}

}

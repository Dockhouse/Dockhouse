package org.dockhouse.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.dockhouse.Application;
import org.dockhouse.config.MongoConfiguration;
import org.dockhouse.domain.RegistryType;
import org.dockhouse.repository.RegistryTypeRepository;
import org.dockhouse.web.rest.dto.RegistryTypeInDTO;
import org.dockhouse.web.rest.dto.RegistryTypeOutDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Test class for the RegistryType service.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Import(MongoConfiguration.class)
public class RegistryTypeServiceTest {

    @Inject
    private RegistryTypeRepository registryTypeRepository;

    @Inject
    private RegistryTypeService registryTypeService;
    
    private RegistryType registryType1;

    private RegistryType registryType2;
    
    @Before
    public void setup() {
        registryTypeRepository.deleteAll();
        
        registryType1 = new RegistryType();
        registryType1.setName("name1");
        registryType1.setLogo("http://example.com/logo.png");
        registryType1.setHost("host");
        registryType1.setPort(2222);
        registryType1.setPublic(false);
        registryTypeRepository.save(registryType1);
        
        registryType2 = new RegistryType();
        registryType2.setName("name2");
        registryType2.setLogo("http://example.com/logo.png");
        registryType2.setHost("host");
        registryType2.setPort(2222);
        registryType2.setPublic(false);
        registryTypeRepository.save(registryType2);
    }

    @Test
    public void getAllTest() {
    	List<RegistryTypeOutDTO> registryTypeOutDTOs = registryTypeService.getAll();
    	assertEquals(2, registryTypeOutDTOs.size());
    }
    
    @Test
    public void getOneTest() {
    	Optional<RegistryTypeOutDTO> registryTypeOutDTO = registryTypeService.getOne(registryType1.getId());
    	assertTrue(registryTypeOutDTO.isPresent());
    	assertEquals(registryType1.getId(), registryTypeOutDTO.get().getId());
    
    	registryTypeOutDTO = registryTypeService.getOne("0");
    	assertFalse(registryTypeOutDTO.isPresent());
    }
    
    @Test
    public void saveTest() {
    	long collectionSize = registryTypeRepository.count();
    	RegistryTypeInDTO registryTypeInDTO = new RegistryTypeInDTO();
    	registryTypeInDTO.setName("new");
    	RegistryTypeOutDTO registryTypeOutDTO = registryTypeService.save(registryTypeInDTO);
    	assertEquals(registryTypeInDTO.getName(), registryTypeOutDTO.getName());
    	assertEquals(collectionSize+1, registryTypeRepository.count());
    }
}

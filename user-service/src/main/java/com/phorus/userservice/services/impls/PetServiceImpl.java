package com.phorus.userservice.services.impls;

import com.phorus.userservice.config.CustomProperties;
import com.phorus.userservice.exceptions.BadRequestException;
import com.phorus.userservice.exceptions.ResourceNotFoundException;
import com.phorus.userservice.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Class only used for communicating with pet-service
 */
@Service
public class PetServiceImpl implements PetService {

    private final RestTemplateBuilder restTemplate;
    private final CustomProperties customProperties;

    @Autowired
    public PetServiceImpl(RestTemplateBuilder restTemplate, CustomProperties customProperties) {
        this.restTemplate = restTemplate;
        this.customProperties = customProperties;
    }

    @Override
    public Long getAmountOfPets(Long userId) {

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(customProperties.getPetServiceUrl() + "/pets/getAmountOfPets")
                .queryParam("userId", userId);

        System.out.println(builder.toUriString());
        ResponseEntity<Long> response = restTemplate.build().getForEntity(builder.toUriString(), Long.class);

        return response.getBody();
    }
}

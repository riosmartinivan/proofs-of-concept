package com.phorus.userservice.services.impls;

import com.phorus.userservice.config.CustomProperties
import com.phorus.userservice.exceptions.BadRequestException
import com.phorus.userservice.exceptions.ResourceNotFoundException
import com.phorus.userservice.services.PetService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange

/**
 * Class only used for communicating with pet-service
 */
@Service
class PetServiceImpl(
    webClientBuilder: WebClient.Builder,
    customProperties: CustomProperties,
) : PetService {

    private val webClient: WebClient = webClientBuilder.baseUrl(customProperties.petServiceUrl).build()

    override suspend fun getAmountOfPets(userId: Long): Long =
        webClient.get()
            .uri { it.path("/pets/getAmountOfPets")
                .queryParam("userId", userId)
                .build()}
            .awaitExchange {
                when (it.statusCode()) {
                    HttpStatus.OK -> it.awaitBody(Long::class)
                    HttpStatus.NOT_FOUND -> throw ResourceNotFoundException("User not found with id: $userId")
                    else -> throw BadRequestException("Error")
                }
            }
}

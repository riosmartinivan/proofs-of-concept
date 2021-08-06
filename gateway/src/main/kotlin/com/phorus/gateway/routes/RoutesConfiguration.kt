package com.phorus.gateway.routes

import com.phorus.gateway.config.RoutePaths
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.ServerWebExchange
import java.net.URI
import java.util.*

@Configuration
class RoutesConfiguration(val routePaths: RoutePaths) {

    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator =
        builder.routes()
            .route("route_services") { r: PredicateSpec ->
                r.path("/api/**")
                    .filters { filterSpec ->
                        filterSpec.changeRequestUri { swe: ServerWebExchange ->
                            val uriString = swe.request.uri.toString().split("//")
                                .let { if (it.size < 2) it[0] else it[1] }
                            val baseUrl = uriString.split("/")[0]
                            val service = uriString.split("/")[2]

                            val path = uriString.replace("$baseUrl/api/$service", "")
                            val serviceURL = routePaths.paths?.get(service) ?: "$service:8080"

                            println("Final uri http://$serviceURL$path")

                            Optional.of(URI("http://$serviceURL$path"))
                        }
                    }
                    .uri("no://op")
            }
            .build()
}
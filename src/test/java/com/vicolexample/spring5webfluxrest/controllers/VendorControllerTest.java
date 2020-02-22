package com.vicolexample.spring5webfluxrest.controllers;

import com.vicolexample.spring5webfluxrest.domain.Category;
import com.vicolexample.spring5webfluxrest.domain.Vendor;
import com.vicolexample.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class VendorControllerTest {
    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void list() {
        BDDMockito.given(vendorRepository.findAll()).willReturn(Flux.just(
                Vendor.builder().firstName("FN").lastName("LN").build(),
                Vendor.builder().firstName("FN2").lastName("LN2").build()));

        webTestClient.get().uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        BDDMockito.given(vendorRepository.findById("id")).willReturn(Mono.just(
                Vendor.builder().firstName("FN").lastName("LN").build()));

        webTestClient.get().uri("/api/v1/vendors/id")
                .exchange()
                .expectBody(Category.class);
    }
}
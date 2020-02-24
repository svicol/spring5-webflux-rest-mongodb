package com.vicolexample.spring5webfluxrest.controllers;

import com.vicolexample.spring5webfluxrest.domain.Category;
import com.vicolexample.spring5webfluxrest.domain.Vendor;
import com.vicolexample.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        given(vendorRepository.findAll()).willReturn(Flux.just(
                Vendor.builder().firstName("FN").lastName("LN").build(),
                Vendor.builder().firstName("FN2").lastName("LN2").build()));

        webTestClient.get().uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        given(vendorRepository.findById("id")).willReturn(Mono.just(
                Vendor.builder().firstName("FN").lastName("LN").build()));

        webTestClient.get().uri("/api/v1/vendors/id")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    void create() {
        given(vendorRepository.saveAll(any(Publisher.class))).willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().firstName("FN").lastName("LN").build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void update() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("FN").lastName("LN").build());

        webTestClient.put()
                .uri("/api/v1/vendors/id")
                .body(vendorToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testPatchVendorWithFirstNameChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToPatchMono = Mono.just(Vendor.builder().firstName("FN").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/id")
                .body(vendorToPatchMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(vendorRepository, times(1)).save(any());
    }

    @Test
    void testPatchVendorWithLastNameChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().build()));
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToPatchMono = Mono.just(Vendor.builder().lastName("LN").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/id")
                .body(vendorToPatchMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(vendorRepository).save(any());
    }

    @Test
    void testPatchVendorWithNoChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("FN").lastName("LN").build()));
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToPatchMono = Mono.just(Vendor.builder().firstName("FN").lastName("LN").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/id")
                .body(vendorToPatchMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(vendorRepository, never()).save(any());
    }
}
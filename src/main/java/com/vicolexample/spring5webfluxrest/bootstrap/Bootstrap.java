package com.vicolexample.spring5webfluxrest.bootstrap;

import com.vicolexample.spring5webfluxrest.domain.Category;
import com.vicolexample.spring5webfluxrest.domain.Vendor;
import com.vicolexample.spring5webfluxrest.repositories.CategoryRepository;
import com.vicolexample.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(categoryRepository.count().block() == 0) {
            categoryRepository.save(Category.builder().description("Category_1").build()).block();
            categoryRepository.save(Category.builder().description("Category_2").build()).block();
            categoryRepository.save(Category.builder().description("Category_3").build()).block();
            categoryRepository.save(Category.builder().description("Category_4").build()).block();
            categoryRepository.save(Category.builder().description("Category_5").build()).block();

            System.out.println("###### Categories inserted ######");

            vendorRepository.save(Vendor.builder().firstName("John").lastName("Boss").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Martin").lastName("King").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Sarah").lastName("White").build()).block();
            vendorRepository.save(Vendor.builder().firstName("George").lastName("First").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Greg").lastName("Ford").build()).block();

            System.out.println("###### Vendors inserted ######");
        }

        System.out.println("Total number of Categories " + categoryRepository.count().block());
        System.out.println("Total number of Vendors " + vendorRepository.count().block());
    }
}

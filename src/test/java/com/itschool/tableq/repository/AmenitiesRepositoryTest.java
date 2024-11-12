package com.itschool.tableq.repository;

import com.itschool.tableq.ApplicationTests;
import com.itschool.tableq.domain.Amenity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AmenitiesRepositoryTest extends ApplicationTests {
    @Autowired
    private AmenitiesRepository amenitiesRepository;

    @BeforeEach
    void deleteEntities() {
        amenitiesRepository.deleteAll();
    }

    @Test
    @Transactional
    void createAmenities() {
        Amenity amenity = Amenity.builder()
                .name("테스트")
                .build();

        Amenity saveAmenity =  amenitiesRepository.save(amenity);

        System.out.println("saved amenity: " + amenity);

        assertThat(saveAmenity);
        assertThat(saveAmenity.getName()).isEqualTo(amenity.getName());
    }

    @Test
    @Transactional
    void updateAmenities() {
        Amenity amenity = Amenity.builder()
                .name("테스트")
                .build();

        Amenity saveAmenity = amenitiesRepository.save(amenity);
        System.out.println("saved amenity: " + amenity);
        System.out.println("savedAmenity.getName(): " + saveAmenity.getName());

        saveAmenity.setName("테스트업데이트");

        Amenity updateAmenity = amenitiesRepository.saveAndFlush(saveAmenity);

        System.out.println("updated amenity: " + updateAmenity);
        System.out.println("updateAmenity.getName(): " + updateAmenity.getName());

        assertThat(updateAmenity).isNotNull();
        assertThat(updateAmenity.getName()).isEqualTo("테스트업데이트");
    }

    @Test
    @Transactional
    void deleteAmenities() {
        Long beforeCount = amenitiesRepository.countBy().orElseThrow();

        Amenity amenity = Amenity.builder()
                .name("Test")
                .build();

        Amenity saveAmenity = amenitiesRepository.save(amenity);

        amenitiesRepository.delete(amenity);

        Long afterCount = amenitiesRepository.countBy().orElseThrow();

        assertThat(afterCount - beforeCount).isEqualTo(0);
    }
}
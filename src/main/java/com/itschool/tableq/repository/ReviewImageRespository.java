package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewImageRespository extends JpaRepository<ReviewImage, Long> {
    Optional<List<ReviewImage>> findByReview(Review review);
}

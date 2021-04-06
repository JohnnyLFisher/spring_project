package com.codeup.springproject.repo;

import com.codeup.springproject.models.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ad, Long> {
}
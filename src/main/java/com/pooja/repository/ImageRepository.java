package com.pooja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.user.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>{
    Image findByGeneratedId(String generatedId);

    @Transactional
    @Modifying
    @Query("delete from Image i where i.imageId=?1")
    void deleteByImageId(long imageId);
}

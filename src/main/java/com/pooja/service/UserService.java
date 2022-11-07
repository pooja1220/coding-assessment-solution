package com.pooja.service;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.user.model.Image;
import com.user.model.User;
import com.user.model.dto.GetImageDTO;

public interface UserService {
	void save(User user);

    User findByUsername(String username);

    String addImage(MultipartFile file,Principal principal);

    List<GetImageDTO> getImages(Principal principal);

    String deleteImage(String imageId,Principal principal);

    List<User> getUsers();
    List<Image> getImages();
}
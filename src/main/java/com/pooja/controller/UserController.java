package com.pooja.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pooja.service.UserService;
import com.user.model.Image;
import com.user.model.User;
import com.user.model.dto.GetImageDTO;

@RestController
public class UserController {
	@Autowired
    private UserService userService;
	
	@PostMapping("/registration")
    public String registration(@RequestBody User user) {
        try{
        userService.save(user);
        }catch(Exception ex){
           return "Error in registration. Please try again!!!";
        }
        return "Registration successfull!!!";
    }

    @PostMapping("/addImage")
    public String addImage(@RequestParam MultipartFile image, Principal principal){
        String response = userService.addImage(image,principal);
        return response.equals("200")?"Added Image Sucessfully":"Failed to add Image";
    }

    @GetMapping("/getImages")
    public List<GetImageDTO> getImage(Principal principal){
        return userService.getImages(principal);
    }

    @DeleteMapping("/deleteImage")
    public String deleteImage(@RequestParam String imageId, Principal principal){
        String response = "";
        try{
           response = userService.deleteImage(imageId, principal);
        }catch(Exception e){
            e.printStackTrace();
           return "Error Ocurred";
        }
        return response.equals("200")?"Deleted Successfully":"Error ocurred in client server";
    }

    //testing
    @GetMapping("/getImagesUser")
    public List<Image> getUsers(){
        return userService.getImages();
    }
	
}


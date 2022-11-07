package com.pooja.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.pooja.repository.ImageRepository;
import com.pooja.repository.UserRepository;
import com.user.model.Image;
import com.user.model.User;
import com.user.model.dto.DeleteDTO;
import com.user.model.dto.GetImageDTO;
import com.user.model.dto.UploadDTO;
@Service
public class UserServiceImpl implements UserService {

	@Value("${imgur.secret}")
	private String imgurSecret;

	@Value("${imgur.image.upload.url}")
	private String addImageUrl;

	@Value("${imgur.image.getOrDelete.url}")
	private String getOrDeleteImageUrl;

	@Autowired
    private UserRepository userRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);
	}
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> getUsers(){
		return userRepository.findAll();
	}
	@Override
	public List<GetImageDTO> getImages(Principal principal) {
		List<GetImageDTO> imagesList = new ArrayList<>();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Client-ID "+imgurSecret);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		if(checkIfImagesExistForUser(principal.getName())){
            for(Image image: userRepository.findByUsername(principal.getName()).getImages()){
                GetImageDTO imageRetreived = restTemplate.exchange(getOrDeleteImageUrl+image.getGeneratedId(), HttpMethod.GET, entity, GetImageDTO.class).getBody();
			    imagesList.add(imageRetreived);
			}
		}
        return imagesList;
	}
	@Override
	public String addImage(MultipartFile file, Principal principal) {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("image", file.getResource());	
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Client-ID "+imgurSecret);
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<LinkedMultiValueMap<String, Object>> entity = new    HttpEntity<LinkedMultiValueMap<String, Object>>(
                    map, headers);
		UploadDTO uploadData = restTemplate.exchange(addImageUrl, HttpMethod.POST, entity, UploadDTO.class).getBody();
        Image image = populateImageDetails(uploadData,principal.getName());
	    imageRepository.save(image);
		return uploadData.getStatus();
	}

	private Image populateImageDetails(UploadDTO uploadData, String username){
		Image image= new Image();
		User user = userRepository.findByUsername(username);
		image.setDeleteImageId(uploadData.getData().getDeletehash());
		image.setGeneratedId(uploadData.getData().getId());
		image.setUser(user);
		return image;
	}
	@Override
	public List<Image> getImages() {
		return imageRepository.findAll();
	}
	@Override
	public String deleteImage(String imageId, Principal principal) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Client-ID "+imgurSecret);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		Image image = retreiveImage(imageId, principal);
        ResponseEntity<DeleteDTO> responseData = restTemplate.exchange(getOrDeleteImageUrl+image.getDeleteImageId(), HttpMethod.DELETE, entity, DeleteDTO.class);
		if(responseData.getStatusCode().is2xxSuccessful()){
			deleteImagesInDB(imageId);
			return responseData.getBody().getStatus();
		}
		return "Error";
	}

	private Image retreiveImage(String imageId, Principal principal){
		User user = userRepository.findByUsername(principal.getName());
		if(user != null && !user.getImages().isEmpty()){
          Optional<Image> image = user.getImages().stream().filter(x->x.getGeneratedId().equals(imageId)).findFirst();
		  if(image.isPresent()){
            return image.get();
		  }
		}
        return null;
	}

	private boolean checkIfImagesExistForUser(String username){
        User user = userRepository.findByUsername(username);
		if(user != null && !user.getImages().isEmpty()){
        return true;
		}
		return false;
	}

	private void deleteImagesInDB(String imageid){
         Image image = imageRepository.findByGeneratedId(imageid);
		 if(image != null){
           imageRepository.deleteByImageId(image.getImageId().longValue());
		 }
	}

}

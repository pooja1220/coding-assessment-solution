
non-authenticated:
1. Registration: https://localhost:8081/registration => to register users. In Body=> raw=>{"username":"","password":""}

authenticated:
2. Add Image: https://localhost:8081/addImage => To add image for authenticated user. In Authorization tab, select Basic Auth and enter username and password given at registration time. In Body => form-data(key->image, value->select image file). Change the key type to file.
3. Get Image: https://localhost:8081/getImages => Get images added by authenticated user. In Authorization tab, select Basic Auth and enter username and password given at registration time.
4. Delete Image: https://localhost:8081/deleteImage?imageId=value => Delete image added by authenticated user. In Authorization tab, select Basic Auth and enter username and password given at registration time. In URL in place of value put imageId(imageHash value is to entered not deleteHash).
package com.user.model.dto;

public class GetImageDTO {

    private String success;
    private String status;
    private GetImageDataDTO data;
    
    public String getSuccess() {
        return success;
    }
    public void setSuccess(String success) {
        this.success = success;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public GetImageDataDTO getData() {
        return data;
    }
    public void setData(GetImageDataDTO data) {
        this.data = data;
    }

    
    
}

package com.rentahall.hallservice.service;

import com.rentahall.hallservice.dto.HallRegistrationRequest;
import com.rentahall.hallservice.dto.HallRegistrationResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HallService {
    public HallRegistrationResponse createHall(HallRegistrationRequest request,  List<MultipartFile> images);
    public List<HallRegistrationResponse> getAllHalls();
}

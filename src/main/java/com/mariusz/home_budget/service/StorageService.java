package com.mariusz.home_budget.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface StorageService {

    public Resource loadFile(String filename);
    public void deleteAll(String path);
    public boolean store(MultipartFile file, String fileName , String fileLocation);

}

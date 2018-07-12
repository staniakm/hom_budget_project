package com.mariusz.home_budget.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface StorageService {

    Resource loadFile(String filename);
    void deleteAll(String path);
    boolean store(MultipartFile file, String fileName, String fileLocation);

}

package com.whernandez.dreamshops.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.whernandez.dreamshops.dto.ImageDto;
import com.whernandez.dreamshops.model.Image;

public interface IImageService {

    Image getImage(Long id);

    void deleteImageById(Long id);

    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);

    void updateImage(MultipartFile file, Long imageId);

}

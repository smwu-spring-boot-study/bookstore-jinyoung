package SpringAPIStudy.bookstore.app.item.service.impl;

import SpringAPIStudy.bookstore.app.item.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;
    }

    @Override
    public void deleteFile(String filePath) throws NoSuchElementException {
        File deleteFile = new File(filePath);
        if(deleteFile.exists()) {
            deleteFile.delete();
        } else {
            throw new NoSuchElementException("File Not Found");
        }
    }
}

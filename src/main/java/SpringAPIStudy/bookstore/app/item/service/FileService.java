package SpringAPIStudy.bookstore.app.item.service;

import java.util.NoSuchElementException;

public interface FileService {

    String uploadFile(String uploadPath, String originalFileName,
                      byte[] fileData) throws Exception;

    void deleteFile(String filePath) throws NoSuchElementException;
}

package com.pawsandhands.FileStorage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    public void init();

    public void save(MultipartFile file, Path path, String pathFileUser);
    public void saveTempFile(MultipartFile file, String pathFileUser);
    public String base64EncodedString(MultipartFile file) throws IOException;
    public void base64DecodedString(String encodedString, String outputFileName, Path path) throws IOException;
//    private boolean isContentEquals(MultipartFile file);

    public boolean isFileExist(Path path);
    public void delete(Path path);

    public void deleteAll();

//    public String getContentType(MultipartFile file) throws IOException;
//
//    public Stream<Path> loadAll();
}

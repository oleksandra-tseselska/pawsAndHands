package com.pawsandhands.FileStorage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    public void init(Path path);

    public void save(MultipartFile file, Path path);
    public String getContentType(MultipartFile file) throws IOException;
    public String base64EncodedString(MultipartFile file) throws IOException;
    public void base64DecodedString(String encodedString, String outputFileName, Path path) throws IOException;



//    public Resource load(String filename);
//
//    public void deleteAll();
//
//    public void delete(Path path);
//
//    public Stream<Path> loadAll();
}

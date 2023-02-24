package com.pawsandhands.FileStorage;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl  implements FileStorageService{

    private Path pathTempImg = Paths.get("src/main/resources/static/img/tempImg");

    //    Now we do not need this because we have these folders on PS (problem with placeholder)
    @Override
    public void init() {
        try {
            Files.createDirectories(pathTempImg);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

//    Start
    @Override
    public void save(MultipartFile file, Path path, String pathFileUser) {
        try {

//          is existed
            if (isFileExist(path)) {
                File existFile = path.toFile();
                saveTempFile(file, pathFileUser);
                File inputFileTemp = Paths.get(pathTempImg+File.separator+pathFileUser).toFile();

                if(!FileUtils.contentEquals(inputFileTemp, existFile)) {
                    init();
                    delete(path);
                    save(file, path, "");
                    deleteAll();
//                    delete(inputFileTemp.toPath());
                }
            } else {
                Files.copy(file.getInputStream(), path);
            }


        } catch (Exception e) {
            if(e instanceof FileSizeLimitExceededException){
                throw new RuntimeException("A file is too big.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void saveTempFile(MultipartFile file, String pathFileUser) {
       Path path = Paths.get(pathTempImg+File.separator+pathFileUser);
        try {
            Files.copy(file.getInputStream(), path);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void delete(Path path) {
        FileSystemUtils.deleteRecursively(path.toFile());
    }

    @Override
    public String base64EncodedString(MultipartFile file) throws IOException{
            byte[] photoByte = file.getBytes();
            String encodedString = Base64.getEncoder().encodeToString(photoByte);

            return encodedString;
    }

        @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(pathTempImg.toFile());
    }

    @Override
    public void base64DecodedString(String encodedString, String outputFileName, Path path) throws IOException{
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        File outputFile = new File(path+File.separator+outputFileName);
        FileUtils.writeByteArrayToFile(outputFile, decodedBytes);
    }
    @Override
    public boolean isFileExist(Path path){
        boolean result;
        try{
            Resource resource = new UrlResource(path.toUri());
            result = (resource.exists() || resource.isReadable());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        return result;
    }

    //    @Override
//    public String getContentType(MultipartFile file) throws IOException{
//        String contentTypeFile = null;
//
//        switch(file.getContentType()) {
//            case "image/png":
//                contentTypeFile = "png";
//                break;
//            case "image/jpeg":
//                contentTypeFile = "jpg";
//                break;
//        }
//
//        return contentTypeFile;
//    }

//    @Override
//    public Resource load(String filename) {
//        try {
//            Path file = root.resolve(filename);
//            Resource resource = new UrlResource(file.toUri());
//
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            } else {
//                throw new RuntimeException("Could not read the file!");
//            }
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Error: " + e.getMessage());
//        }
//    }

//    @Override
//    public Stream<Path> loadAll() {
//        try {
//            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
//        } catch (IOException e) {
//            throw new RuntimeException("Could not load the files!");
//        }
//    }
}

package com.pawsandhands.FileStorage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.swing.*;

@Service
public class FilesStorageServiceImpl  implements FileStorageService{
//    Now we do not need this because we have these folders on PS (problem with placeholder)
    @Override
    public void init(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

//    Start
    @Override
    public void save(MultipartFile file, Path path) {
        try {
            Files.copy(file.getInputStream(), path);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public String getContentType(MultipartFile file) throws IOException{
        String contentTypeFile = null;

        switch(file.getContentType()) {
            case "image/png":
                contentTypeFile = "png";
                break;
            case "image/jpeg":
                contentTypeFile = "jpg";
                break;
        }

        return contentTypeFile;
    }

    @Override
    public String base64EncodedString(MultipartFile file) throws IOException{
            byte[] photoByte = file.getBytes();
            String encodedString = Base64.getEncoder().encodeToString(photoByte);

            return encodedString;
    }

    @Override
    public void base64DecodedString(String encodedString, String outputFileName, Path path) throws IOException{
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        File outputFile = new File(path+File.pathSeparator+outputFileName);
//        ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
//        BufferedImage copyImg = ImageIO.read(bis);
//        ImageIO.write(copyImg, "jpg", new File("output.jpg") );
//
////        ImageIcon imageIcon = new ImageIcon(decodedBytes);
////
////        Image image = imageIcon.getImage();
        FileUtils.writeByteArrayToFile(outputFile, decodedBytes);
    }


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
//    public void deleteAll() {
//        FileSystemUtils.deleteRecursively(root.toFile());
//    }
//
//    @Override
//    public void delete(Path path) {
//        FileSystemUtils.deleteRecursively(path.toFile());
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

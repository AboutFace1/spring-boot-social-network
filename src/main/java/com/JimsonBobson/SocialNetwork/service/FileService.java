package com.JimsonBobson.SocialNetwork.service;

import com.JimsonBobson.SocialNetwork.exceptions.ImageTooSmallException;
import com.JimsonBobson.SocialNetwork.exceptions.InvalidFileException;
import com.JimsonBobson.SocialNetwork.model.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Random;

@Service
public class FileService {

    @Value("${photo.file.extensions}")
    private String imageExtensions;

    private Random random = new Random();

    private String getFileExtension(String filename) {
        int dotPosition = filename.lastIndexOf(".");

        if(dotPosition < 0) {
            return null;
        }

        return filename.substring(dotPosition+1).toLowerCase();
    }

    private boolean isImageExtension(String extension) {

        String testExtension = extension.toLowerCase();

        for (String validExtension: imageExtensions.split(",")) {
            if (testExtension.equals(validExtension)) {
                return true;
            }
        }

        return false;

    }

    private File makeSubdirectory(String basePath, String prefix) {
        int nDirectory = random.nextInt(1000);
        String sDirectory = String.format("%s%03d", prefix, nDirectory);

        File directory = new File(basePath, sDirectory);

        if (!directory.exists()) {
            directory.mkdir();
        }

        return directory;
    }

    public FileInfo saveImageFile(MultipartFile file, String baseDirectory, String subDirPrefix, String filePrefix, int width, int height)
            throws InvalidFileException, IOException, ImageTooSmallException {
        int nFilename = random.nextInt(1000);
        String filename = String.format("%s%03d", filePrefix, nFilename);

        String extension = getFileExtension(file.getOriginalFilename());

        if (extension==null) {
            throw new InvalidFileException("No file extension");
        }

        if (!isImageExtension(extension)) {
            throw new InvalidFileException("Not an image file");
        }

        File subDirectory = makeSubdirectory(baseDirectory, subDirPrefix);

        Path filepath = Paths.get(subDirectory.getCanonicalPath(), filename + "." + extension);

        BufferedImage resizedImage = resizeImage(file, width, height);

        ImageIO.write(resizedImage, extension, filepath.toFile());

        return new FileInfo(filename, extension, subDirectory.getName(), baseDirectory);
    }

    private BufferedImage resizeImage(MultipartFile inputFile, int width, int height) throws IOException, ImageTooSmallException {
        BufferedImage image = ImageIO.read(inputFile.getInputStream());

        if (image.getWidth() < width || image.getHeight() < height) {
            throw new ImageTooSmallException();
        }

        double widthScale = (double)width/image.getWidth();
        double heightScale = (double)height/image.getHeight();

        double scale = Math.max(widthScale, heightScale);

        BufferedImage scaleImage = new BufferedImage((int)(scale * image.getWidth()), (int)(scale * image.getHeight()), image.getType());

        Graphics2D g = scaleImage.createGraphics();

        AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);

        g.drawImage(image, transform, null);

        return scaleImage.getSubimage(0,0, width, height);
    }
}

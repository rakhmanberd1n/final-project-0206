package nazym.project.controllers;


import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Controller
public class PictureController
{
    @Value("${targetPictureUrl}")
    private String targetPictureUrl;

    @Value("${loadPictureUrl}")
    private String loadPictureUrl;


    @GetMapping(value = "/getPicture/{picture}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getPicture(@PathVariable String picture) throws IOException
    {
        InputStream in = new ClassPathResource(loadPictureUrl + picture).getInputStream();
        return IOUtils.toByteArray(in);

    }

    public void addPictureLocal(MultipartFile picture) throws IOException
    {
        byte[] bytes = picture.getBytes();
        Path path = Paths.get(targetPictureUrl, picture.getOriginalFilename());
        Files.write(path, bytes);
    }
}
package com.rezilux.dinngdonngecommerceapi.utils;

//import com.paypal.base.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Transactional
public class FileUploadService {

    @Value("${app.assets.path}")
    private String rootPath;

    public String upload(String folder, String content, String extension) {
        byte[] data = Base64.decodeBase64(content);
        String path = "";

        try {
            path = "/" + folder + "/"
                    + UUID.randomUUID().toString()
                    + formatDate(true) + "." + extension;
            OutputStream stream = new FileOutputStream(rootPath + path);
            stream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public String formatDate(Boolean forName) {
        DateTimeFormatter dtf = forName ? DateTimeFormatter.ofPattern("ddMMyyyyHHmmss") :
                DateTimeFormatter.ofPattern("EEE dd MMM yyyy HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }
}

package de.storagesystem.api.storage;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * @author Simon Brebeck on 28.11.2022
 */
public class ByteArrayMultipartFile implements MultipartFile {
    private final ByteArrayResource resource;
    private final String name;
    private final String contentType;

    public ByteArrayMultipartFile(String name, String contentType, ByteArrayResource resource) {
        this.resource = resource;
        this.name = name;
        this.contentType = contentType;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return resource.getFilename();
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return resource.contentLength() == 0;
    }

    @Override
    public long getSize() {
        return resource.contentLength();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return resource.getByteArray();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return resource.getInputStream();
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        MultipartFile.super.transferTo(dest.toPath());
    }

    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {
        MultipartFile.super.transferTo(dest);
    }


}

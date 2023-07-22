package spin.sns.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import spin.sns.domain.image.Image;
import spin.sns.domain.post.Post;
import spin.sns.repository.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final String FILE_DIR = "C:\\Users\\josey\\Desktop\\spin_file_dir\\";

    @Autowired
    private final ImageRepository imageRepository;

    public void saveImage(List<MultipartFile> files, Post post) {
        if (!files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                try {
                    String serverSaveFileName = createServerFileName(file.getOriginalFilename());
                    Image image = Image.builder()
                            .post(post)
                            .serverFilename(serverSaveFileName)
                            .originalFilename(file.getOriginalFilename())
                            .build();
                    imageRepository.save(image);
                    file.transferTo(new File(getServerSaveFileFullPath(serverSaveFileName)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getServerSaveFileFullPath(String filename) {
        return FILE_DIR + filename;
    }

    private String createServerFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + extractExt(originalFilename);
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}

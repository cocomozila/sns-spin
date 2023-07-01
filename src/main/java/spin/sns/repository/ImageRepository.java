package spin.sns.repository;

import spin.sns.domain.image.Image;

public interface ImageRepository {

    public Image save(Image image);
}

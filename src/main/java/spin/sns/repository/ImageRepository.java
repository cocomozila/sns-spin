package spin.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spin.sns.domain.image.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}

package SpringAPIStudy.bookstore.app.user.repository;

import SpringAPIStudy.bookstore.app.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User getById(Long id);

    Optional<User> findBysocialId(String socialId);


//    @Query("SELECT u.refreshToken FROM User u WHERE u.id=:id")
//    String getRefreshTokenById(@Param("id") Long id);

    @Query("SELECT u.refreshToken FROM User u WHERE u.socialId=:socialId")
    String getRefreshTokenBySocialId(@Param("socialId") String socialId);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.refreshToken=:token WHERE u.socialId=:socialId")
    void updateRefreshToken(@Param("socialId") String socialId, @Param("token") String token);


}

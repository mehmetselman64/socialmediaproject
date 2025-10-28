package com.mehmetselman.socialmediaproject.repository;

import com.mehmetselman.socialmediaproject.entity.Like;
import com.mehmetselman.socialmediaproject.entity.Post;
import com.mehmetselman.socialmediaproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ✅ LikeRepository
 *
 * Bu repository, kullanıcıların gönderilere yaptığı beğenme (Like) işlemlerini yönetir.
 *
 * `JpaRepository` sayesinde CRUD işlemleri (create, read, update, delete) otomatik olarak sağlanır.
 *
 * Ek olarak:
 *  - Belirli bir kullanıcı bir gönderiyi beğenmiş mi kontrol edilebilir.
 *  - Kullanıcının belirli bir gönderi üzerindeki Like nesnesi bulunabilir.
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * 🔹 Kullanıcının belirli bir gönderiyi beğenip beğenmediğini kontrol eder.
     *
     * @param user Kullanıcı nesnesi
     * @param post Gönderi nesnesi
     * @return true → kullanıcı gönderiyi beğenmiş, false → beğenmemiş
     */
    boolean existsByUserAndPost(User user, Post post);

    /**
     * 🔹 Belirli bir kullanıcı ile gönderi arasındaki Like kaydını döner.
     *
     * @param user Kullanıcı nesnesi
     * @param post Gönderi nesnesi
     * @return Like nesnesi (varsa), aksi halde null
     */
    Like findByUserAndPost(User user, Post post);
}

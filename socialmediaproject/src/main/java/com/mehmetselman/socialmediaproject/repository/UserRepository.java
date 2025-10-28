package com.mehmetselman.socialmediaproject.repository;

import com.mehmetselman.socialmediaproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ✅ UserRepository
 *
 * Bu repository sınıfı, `User` entity’siyle ilgili veritabanı işlemlerini yönetir.
 *
 * Spring Data JPA sayesinde aşağıdaki işlemler otomatik olarak yapılabilir:
 *  - Kullanıcı ekleme (save)
 *  - Kullanıcı silme (delete)
 *  - Kullanıcı güncelleme (save)
 *  - Kullanıcıyı ID’ye göre bulma (findById)
 *  - Tüm kullanıcıları listeleme (findAll)
 *
 * Ek olarak özel bir metot tanımlanmıştır:
 *  - `findByUsername()` → Kullanıcı adını (username) kullanarak kullanıcıyı bulur.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 🔹 Kullanıcı adını (username) kullanarak kullanıcıyı bulur.
     *
     * Örnek kullanım:
     * ```java
     * User user = userRepository.findByUsername("mehmet");
     * ```
     *
     * @param username Aranan kullanıcı adı
     * @return Eşleşen kullanıcı (varsa), aksi halde null
     */
    User findByUsername(String username);
}

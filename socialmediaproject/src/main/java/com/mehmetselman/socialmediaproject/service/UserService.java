package com.mehmetselman.socialmediaproject.service;

import com.mehmetselman.socialmediaproject.entity.User;
import com.mehmetselman.socialmediaproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //parola guncelleme
    public void updatePassword(User currentUser, String oldPassword, String newPassword){
        if (!currentUser.getPassword().equals(oldPassword)) throw new RuntimeException("Yanlis Parola!");
        currentUser.setPassword(newPassword);
        userRepository.save(currentUser);
    }

   //kullanici kendi hesabini silme
   public void deleteMe(User currentUser){
        userRepository.delete(currentUser);
   }

   //admin herhangi bir kullaniciyi silme
    public void adminDeleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User getUser(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Kullanici bulunamadi!"));
    }

}

package application.repository;

import domain.user.User;

import java.io.IOException;

public interface UserRepositoryInterface {

    public void save(User user) throws IOException; // bu metod içine txt,csv gibi bir dosyanın içine user kaydetme işini yapar.


    public boolean doesExist(String email);//bu metod dosyanın içinde yazılan email kayıtlı mı diye bakıyor.

    public User findByMail(String mail);
}

package application.repository;

import domain.user.User;
import java.io.IOException;

public interface UserRepositoryInterface {
    void save(User user) throws IOException;
    boolean doesExist(String email);
    User findByMail(String mail);
}
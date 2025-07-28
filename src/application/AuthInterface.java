package application;

import domain.user.User;

import java.io.IOException;

public interface AuthInterface {

   User login(String mail, String rawPassword); /*hash mapde tuttuğumuz , ya da bir txt dosyasında tuttuğumuz veriler içinde
     bu kullanıcı var mı ve doğru mu kontrol ettiğimiz, doğruysa sisteme girişini yaptığımız değilse nesi yanlış söylediğimiz fonksiyon.*/

    boolean signUp(String mail, String rawPassword, String name, String surname, String role) throws IOException;/* user kaydetme fonksiyonu, txt dosyasında email kayıtlı mı ve şifre kurallara uygun mu kontrol edip
    her şey doğruysa txt dosyasına yazan fonksiyon.*/

}

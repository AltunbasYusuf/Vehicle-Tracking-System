package Infrastructure;

import application.security.PasswordEncoderInterface;

public class SimplePasswordEncoder implements PasswordEncoderInterface {

    @Override
    public String hash(String rawPassword) {
        // Basit bir fake hash algoritması (sadece örnek!)
        int sum = 0;
        for (int i = 0; i < rawPassword.length(); i++) {
            sum += rawPassword.charAt(i) * (i + 7);
        }
        String reversed = new StringBuilder(rawPassword).reverse().toString();
        return "HASH" + sum + reversed.length() + reversed;
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return hash(rawPassword).equals(hashedPassword);
    }
}

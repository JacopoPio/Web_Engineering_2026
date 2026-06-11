package jakarta_configuration.resources;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String CARATTERI =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";

    private static final SecureRandom random = new SecureRandom();

    public static String generaPassword(int lunghezza) {
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < lunghezza; i++) {
            int index = random.nextInt(CARATTERI.length());
            password.append(CARATTERI.charAt(index));
        }

        return password.toString();
    }
}
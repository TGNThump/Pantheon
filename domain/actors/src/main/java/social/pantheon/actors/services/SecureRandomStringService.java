package social.pantheon.actors.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Locale;

@Service
public class SecureRandomStringService {

    private final SecureRandom random = new SecureRandom();

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String lower = upper.toLowerCase(Locale.ROOT);
    public static final String digits = "0123456789";
    private final char[] symbols = (upper + lower + digits).toCharArray();

    public String generate(int length){
        char[] buf = new char[length];

        for (int i = 0; i < buf.length; ++i)
            buf[i] = symbols[random.nextInt(symbols.length)];

        return new String(buf);
    }
}

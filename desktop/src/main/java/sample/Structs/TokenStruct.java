package sample.Structs;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.TimeZone;

public class TokenStruct {
    private String token;
    private OffsetDateTime expiration;

    public OffsetDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(OffsetDateTime expiration) {
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public TokenStruct()
    {
    }
}

package nl.management.finance.app.data.user;

import java.util.UUID;

import nl.management.finance.app.data.Result;

public interface OAuthAdapter {
    Result<Authentication> authenticate(String code);
}

package com.ylab.auditing.provider;

import java.sql.SQLException;
import java.util.Optional;

public interface CurrentUserProvider {
    Optional<String> getCurrentUserIdentifier() throws SQLException;
}

package com.ylab.auditing.provider;

import java.util.Optional;

public interface CurrentUserProvider {
    Optional<String> getCurrentUserIdentifier();
}

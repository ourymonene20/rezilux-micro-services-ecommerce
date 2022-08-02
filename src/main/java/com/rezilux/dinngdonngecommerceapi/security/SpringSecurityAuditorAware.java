package com.rezilux.dinngdonngecommerceapi.security;


import com.rezilux.dinngdonngecommerceapi.config.Constants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

//import com.rezilux.yupay.config.Constants;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component(value = "springSecurityAuditorAware")
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(Constants.SYSTEM_ACCOUNT));
    }
}

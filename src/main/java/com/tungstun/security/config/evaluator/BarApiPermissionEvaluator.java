package com.tungstun.security.config.evaluator;

import com.tungstun.security.data.model.UserProfile;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

public class BarApiPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)){
            return false;
        }
        Long barId = (Long) targetDomainObject;
        return hasPrivilege(auth, barId, permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(auth, Long.valueOf((String) targetId), permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, Long barId, String permission) {
        UserProfile userProfile = (UserProfile) auth.getPrincipal();
        String role = userProfile.getBarAuthorization().get(barId);
        return role != null && role.equals(permission);
    }
}

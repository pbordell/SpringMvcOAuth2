package com.pbs.springmvc.evaluators;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.pbs.springmvc.service.UserService;

public class PermisosMetodesEvaluator implements PermissionEvaluator {
	
    @Autowired
    private UserService userService;
 
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permissionText) {
    	boolean hasPermision = false;
    	
		try {
			userService.findByName(permissionText.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        return hasPermision;
    }
 
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
	
}
package com.pbs.springmvc.evaluators;

import java.io.Serializable;

import com.pbs.springmvc.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;


public class PermisosMetodesEvaluator implements PermissionEvaluator {

    @Autowired
    private PermissionRepository permissionRepository;

    /**
     * Este método se ejecuta con: @PreAuthorize("hasPermission('/person/', 'READ')")
     *
     * @param auth Contiene el usuario autenticado y sus roles cargados desde la BD
     * @param targetDomainObject Captura el primer String (la ruta: '/person/')
     * @param permission Captura el segundo String (la acción: 'READ')
     */
    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {
        if (auth == null || targetDomainObject == null || permission == null) {
            return false;
        }

        String endpointRequerido = targetDomainObject.toString();
        String accionRequerida = permission.toString();

        // Recorremos las autoridades del token OAuth2 (ej: ['ROLE_ADMIN'])
        for (GrantedAuthority authority : auth.getAuthorities()) {
            String rolUsuario = authority.getAuthority();

            // Tu DML guarda 'admin' o 'user'. Como CustomUserDetailsService le añade 'ROLE_',
            // lo limpiamos y pasamos a minúsculas para que coincida exactamente con la tabla 'roles'
            if (rolUsuario.startsWith("ROLE_")) {
                rolUsuario = rolUsuario.replace("ROLE_", "").toLowerCase();
            }

            // Consultamos si existe el registro en la tabla 'role_endpoints'
            int tieneAcceso = permissionRepository.hasAccessToEndpoint(rolUsuario, endpointRequerido, accionRequerida);

            if (tieneAcceso > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Este método es obligatorio por la interfaz, pero se usa para evaluar IDs (ej: #id, 'Persona', 'READ').
     * En tu esquema actual basado en rutas completas no se utiliza, por lo que retorna false.
     */
    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {
        return false;
    }
	
}
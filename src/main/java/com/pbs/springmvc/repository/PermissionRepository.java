package com.pbs.springmvc.repository;

import com.pbs.springmvc.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Role, Long> {

    @Query(value = "SELECT COUNT(*) FROM role_endpoints re " +
            "JOIN roles r ON re.role_id = r.id " +
            "WHERE r.name = :rolName " +
            "AND :endpoint LIKE CONCAT(re.endpoint, '%') " +
            "AND re.accion = :accion",
            nativeQuery = true)
    int hasAccessToEndpoint(@Param("rolName") String rolName,
                            @Param("endpoint") String endpoint,
                            @Param("accion") String accion);
}

package org.fastlink.userservice.repository;

import org.fastlink.userservice.model.FastlinkPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FPrivilegeRepository extends JpaRepository<FastlinkPrivilege, Long>
{
    FastlinkPrivilege findByName(String name);
}
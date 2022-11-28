package org.fastlink.userservice.repository;

import org.fastlink.userservice.model.FastlinkUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FUserRepository extends JpaRepository<FastlinkUser, Long> {
	//@Query("SELECT u FROM User u WHERE u.email = ?1")
	 Optional<FastlinkUser> findByEmailIgnoreCase(String email);

	 boolean existsByEmail(String email);

	 boolean existsByUsername(String username);

	 Optional<FastlinkUser> findByUsername(String username) throws UsernameNotFoundException;

	 List<FastlinkUser> findByUsernameContaining(String username);

	//User saveUser(User user);

}

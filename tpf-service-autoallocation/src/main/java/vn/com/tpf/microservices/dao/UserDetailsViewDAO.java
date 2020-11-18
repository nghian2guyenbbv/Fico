package vn.com.tpf.microservices.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.tpf.microservices.models.UserDetailView;

import java.util.List;

public interface UserDetailsViewDAO extends JpaRepository<UserDetailView, Long> {

    @Query("SELECT e FROM UserDetailView e WHERE e.teamLeader LIKE :teamLeader AND e.userRole = 'role_user'")
    Page<UserDetailView> findAllUserForLeader(@Param("teamLeader") String teamLeader, Pageable pageable);

    @Query("SELECT e FROM UserDetailView e WHERE e.teamName IN(:teamName) AND e.userRole IN('role_user','role_leader') ")
    Page<UserDetailView> findAllUserForSub(@Param("teamName") List<String> teamName , Pageable pageable);

    UserDetailView findByUserNameAndTeamLeader(String userName, String teamLeader);

    @Query("SELECT e FROM UserDetailView e WHERE e.teamName IN(:teamName) AND e.userRole IN('role_user','role_leader') AND e.userName = :userName ")
    List<UserDetailView> findByUserNameAndTeamName(@Param("userName")String userName, @Param("teamName") List<String> teamName);

    Page<UserDetailView> findAllByTeamLeader(String teamLeader, Pageable pageable);

    List<UserDetailView> findAllByUserName(String userName);

    UserDetailView findByUserName(String userName);

    UserDetailView findByUserRoleAndUserNameAndTeamName(String userRole, String userName, String teamName);

    @Query("SELECT u.teamName " +
            "FROM UserDetailView u " +
            "WHERE u.userRole = 'role_supervisor' " +
            "  AND u.userName = :userName " +
            "  AND u.teamName = :teamName ")
    String findTeamNameSupRole(@Param("userName") String userName, @Param("teamName") String teamName);
}

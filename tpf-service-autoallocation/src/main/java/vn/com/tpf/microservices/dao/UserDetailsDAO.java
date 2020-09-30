package vn.com.tpf.microservices.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.tpf.microservices.models.UserDetail;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserDetailsDAO extends JpaRepository<UserDetail, Long> {

    @Query("SELECT e FROM UserDetail e WHERE e.teamLeader LIKE :teamLeader AND e.userRole = 'role_user'")
    Page<UserDetail> findAllUserForLeader(@Param("teamLeader") String teamLeader, Pageable pageable);

    @Query("SELECT e FROM UserDetail e WHERE e.teamName IN(:teamName1,:teamName2) AND e.userRole IN('role_user','role_leader') ")
    Page<UserDetail> findAllUserForSub(@Param("teamName1") String teamName1 ,@Param("teamName2") String teamName2, Pageable pageable);

    UserDetail findByUserNameAndTeamLeader(String userName, String teamLeader);

    @Query("SELECT e FROM UserDetail e WHERE e.teamName IN(:teamName1,:teamName2) AND e.userRole IN('role_user','role_leader') AND e.userName = :userName ")
    List<UserDetail> findByUserNameAndTeamName(@Param("userName")String userName, @Param("teamName1") String teamName1 ,@Param("teamName2") String teamName2);

    Page<UserDetail> findAllByTeamLeader(String teamLeader, Pageable pageable);

    List<UserDetail> findAllByUserName(String userName);

    UserDetail findByUserRoleAndUserNameAndTeamName(String userRole, String userName, String teamName);

    @Query("SELECT u.teamName " +
            "FROM UserDetail u " +
            "WHERE u.userRole = 'role_supervisor' " +
            "  AND u.userName = :userName " +
            "  AND u.teamName = :teamName ")
    String findTeamNameSupRole(@Param("userName") String userName, @Param("teamName") String teamName);
}

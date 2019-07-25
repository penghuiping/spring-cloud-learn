package com.php25.usermicroservice.server.repository;

import com.php25.common.db.repository.BaseRepository;
import com.php25.usermicroservice.server.model.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * @author penghuiping
 * @date 2017/4/24.
 */
public interface UserRoleRepository extends BaseRepository<UserRole, Long> {

    /**
     * 根据roleId 与 userId 查询
     *
     * @param roleId 角色表主键
     * @param userId 后台管理用户表主键
     * @return
     */
    Optional<UserRole> findOneByRoleIdAndUserId(Long roleId, Long userId);

    /**
     * 根据 userId 查询 List<UserRole>
     *
     * @param userId 后台管理用户表主键
     * @return
     */
    Optional<List<UserRole>> findAllByUserId(Long userId);
}

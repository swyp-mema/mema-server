package com.swyp.mema.domain.badge.repository;

import com.swyp.mema.domain.badge.model.Badge;
import com.swyp.mema.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Boolean existsByBadgeId(Long badgeId);

    Badge findByBadgeId(Long badgeId);

    Badge findByUser(User user);

}

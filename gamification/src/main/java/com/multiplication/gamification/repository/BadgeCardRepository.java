package com.multiplication.gamification.repository;

import com.multiplication.gamification.domain.BadgeCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BadgeCardRepository extends CrudRepository<BadgeCard, Long> {

    /**
     * Retrieve all the badge cards for given users
     * @param userId the id of the user to look for badgecards
     * @retrurn the list of BadgeCards, sorted by most recent.
     */
    List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(final Long userId);
}

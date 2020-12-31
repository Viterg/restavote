package ru.viterg.restavote.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;
import ru.viterg.restavote.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.user.id=?1 AND v.voteDate=?2")
    int delete(int userId, LocalDate day);

    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 AND v.voteDate=?2")
    Vote getByUserIdAndVoteDate(int userId, LocalDate day);

    @Query("SELECT v FROM Vote v WHERE v.restaurant.id=?1 AND v.voteDate=?2")
    List<Vote> getAllByRestaurantIdAndVoteDate(int restaurantId, LocalDate day);
}

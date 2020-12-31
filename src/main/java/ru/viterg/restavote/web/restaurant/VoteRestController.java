package ru.viterg.restavote.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.viterg.restavote.AuthorizedUser;
import ru.viterg.restavote.model.Vote;
import ru.viterg.restavote.repository.VoteRepository;
import ru.viterg.restavote.util.Util;
import ru.viterg.restavote.util.exception.NotFoundException;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    public static final  String REST_URL = "/restaurants/";
    private static final Logger log      = LoggerFactory.getLogger(VoteRestController.class);

    private final VoteRepository repository;

    public VoteRestController(VoteRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/{restId}/vote")
    public void addVote(@PathVariable("restId") int restId, @AuthenticationPrincipal AuthorizedUser authUser) {
        Vote vote = repository.get(authUser.getId(), LocalDate.now(Clock.systemDefaultZone()));
        // repository.delete(authUser.getId(), LocalDate.now(Clock.systemDefaultZone()));
        // if (vote.getRestaurant().getId() != restId) {
        //     repository.save(new Vote(), authUser.getId(), restId);
        // }
        repository.save(Objects.requireNonNullElseGet(vote, Vote::new), authUser.getId(), restId);
        log.info("add vote user.id={}, restaurant.id={}", authUser.getId(), restId);
    }

    @DeleteMapping("/{restId}/vote")
    public void clear(@PathVariable("restId") int restId) {
        repository.deleteAll();
    }
}
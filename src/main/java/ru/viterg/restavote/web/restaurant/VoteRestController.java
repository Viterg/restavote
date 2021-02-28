package ru.viterg.restavote.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.viterg.restavote.AuthorizedUser;
import ru.viterg.restavote.model.Vote;
import ru.viterg.restavote.service.VoteService;

import java.net.URI;
import java.time.*;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    public static final  String REST_URL = "/restaurants/";
    private static final Logger log      = LoggerFactory.getLogger(VoteRestController.class);

    private final VoteService service;

    public VoteRestController(VoteService service) {
        this.service = service;
    }

    @PostMapping("/{restId}/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Vote> makeVote(@PathVariable("restId") int restId,
                                         @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("add vote user.id={}, restaurant.id={}", authUser.getId(), restId);
        Vote created = service.create(authUser.getId(), restId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                          .path("/{restId}/vote/{id}")
                                                          .buildAndExpand(restId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping("/{restId}/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateVote(@PathVariable("restId") int restId,
                           @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("update vote for new restaurant {}", restId);
        Clock sysZone = Clock.systemDefaultZone();
        service.update(authUser.getId(), restId, LocalDate.now(sysZone), LocalTime.now(sysZone));
    }

    @GetMapping("/{restId}/vote")
    public Vote getTodaysUserVote(@PathVariable("restId") int restId,
                                  @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("get vote of user.id={} for restaurant {}", authUser.getId(), restId);
        return service.get(authUser.getId(), LocalDate.now(Clock.systemDefaultZone()));
    }

    @DeleteMapping("/{restId}/vote")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodaysUserVote(@PathVariable("restId") int restId,
                                     @AuthenticationPrincipal AuthorizedUser authUser) {
        log.info("delete vote of user.id={} for restaurant {}", authUser.getId(), restId);
        Clock sysZone = Clock.systemDefaultZone();
        service.delete(authUser.getId(), LocalDate.now(sysZone), LocalTime.now(sysZone));
    }
}
package FantasyBasketball.controllers;

import FantasyBasketball.exceptions.resourceNotFoundException;
import FantasyBasketball.models.FantasyStats;
import FantasyBasketball.services.fantasyStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class fantasyStatsController {

    @Autowired
    fantasyStatsService fantasyStatsService;

    private static final Logger log = LoggerFactory.getLogger(fantasyStatsController.class);

    private final HttpServletRequest request;

    // default constructor for fantasyStatsController
    @Autowired
    public fantasyStatsController(HttpServletRequest request) {this.request = request;}

    @RequestMapping(value = "/fantasyStats", method = RequestMethod.GET)
    public ResponseEntity<?> getFantasyStatsByTemplate(
            @RequestParam(value = "player_id", required = false) Integer player_id,
            @RequestParam(value = "schedule_id", required = false) Integer schedule_id,
            @RequestParam(value = "league_id", required = false) Integer league_id,
            @RequestParam(value = "threeP", required = false) Integer threeP,
            @RequestParam(value = "twoP", required = false) Integer twoP,
            @RequestParam(value = "freeThrows", required = false) Integer freeThrows,
            @RequestParam(value = "rebounds", required = false) Integer rebounds,
            @RequestParam(value = "assists", required = false) Integer assists,
            @RequestParam(value = "blocks", required = false) Integer blocks,
            @RequestParam(value = "steals", required = false) Integer steals,
            @RequestParam(value = "turnovers", required = false) Integer turnovers,
            @RequestParam(value = "totPoints", required = false) Integer totPoints) {

        // TODO: Implement authentication so client_id gets filled up automatically
        Integer client_id = 1;

        // Log request
        log.info("client with id {} is calling getFantasyStatsByTemplate with " +
                        "player_id {}, " +
                        "schedule_id {}, " +
                        "threeP {}, " +
                        "twoP {}, " +
                        "freeThrows {}, " +
                        "rebounds {}, " +
                        "assists {}, " +
                        "blocks {}, " +
                        "steals {}, " +
                        "turnovers {}, " +
                        "totPoints {}",
                client_id,
                player_id,
                schedule_id,
                threeP,
                twoP,
                freeThrows,
                rebounds,
                assists,
                blocks,
                steals,
                turnovers,
                totPoints);

        try {
            List<FantasyStats> result =  fantasyStatsService.getStatsByTemplate(
                    player_id,
                    schedule_id,
                    client_id,
                    league_id,
                    threeP,
                    twoP,
                    freeThrows,
                    rebounds,
                    assists,
                    blocks,
                    steals,
                    turnovers,
                    totPoints);

            log.info("FantasyStats retrieved successfully");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception on GET: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/fantasyStats", method = RequestMethod.POST)
    public ResponseEntity<?> createStats(@RequestParam FantasyStats stats) {

        // TODO: Implement authentication so client_id gets filled up automatically
        Integer client_id = 1;
        log.info("client with id {} is creating FantasyStats with " +
                        "player_id {}, " +
                        "schedule_id {}, ",
                client_id,
                stats.getPlayerID(),
                stats.getScheduleID());
        try {
            List<FantasyStats> result = fantasyStatsService.postStats(stats);
            log.info("Fantasy Stats entry has been created successfully");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            log.error("Exception on POST: ", e);
            return new ResponseEntity<>("This action is not allowed, please check values and try again.", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.error("Exception on POST: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/fantasyStats", method = RequestMethod.PUT)
    public ResponseEntity<?> updateStats(@RequestParam FantasyStats stats) {

        // TODO: Implement authentication so client_id gets filled up automatically
        Integer client_id = 1;
        log.info("client with id {} is updating FantasyStats with " +
                        "player_id {}, " +
                        "schedule_id {}, ",
                client_id,
                stats.getPlayerID(),
                stats.getScheduleID());


        try{
            // TODO: This method will call the information from ball don't lie and update the Fantasy Stats
            List<FantasyStats> result = fantasyStatsService.updateStats(stats);
            log.info("Fantasy Stats entry has been updated successfully");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception on POST: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/fantasyStats", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteStats(@RequestParam Integer player_id,
                                         @RequestParam Integer schedule_id) {

        // TODO: Implement authentication so client_id gets filled up automatically by whoever requests it
        Integer client_id = 1;
        log.info("client with id {} is deleting FantasyStats with " +
                        "player_id {}, " +
                        "schedule_id {}, ",
                client_id,
                player_id,
                schedule_id);
        try {
            fantasyStatsService.deleteStats(player_id, schedule_id);
            log.info("Fantasy Stats entry has been deleted successfully");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (resourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception on POST: ", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

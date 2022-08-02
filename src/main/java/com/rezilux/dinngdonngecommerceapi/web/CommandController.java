package com.rezilux.dinngdonngecommerceapi.web;

import com.rezilux.dinngdonngecommerceapi.entities.Command;
import com.rezilux.dinngdonngecommerceapi.services.CommandService;
import com.rezilux.dinngdonngecommerceapi.specification.CustomSpecificationBuilder;
import com.rezilux.dinngdonngecommerceapi.web.exceptions.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
public class CommandController {

    private final Logger logger = LoggerFactory.getLogger(CommandController.class);
    private final CommandService commandService;

    public CommandController(CommandService commandService) { this.commandService = commandService; }

    /**
     * {@code POST  /commands} : Create a new command.
     *
     * @param command the command to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new command, or with status {@code 400 (Bad Request)} if the command has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws Exception if the file upload process failed or the user is not a merchant.
     */
    @PostMapping("/commands")
    public ResponseEntity<Command> createCommand(@Valid @RequestBody Command command) throws URISyntaxException, Exception {
        if (command.getId() != null) {
            throw new BadRequestAlertException("A new command cannot already have an ID", "Command", "idexists");
        }
        Command result = commandService.create(command);
        return ResponseEntity.created(new URI("/api/commands/" + result.getId()))
                .body(result);
    }

    /**
     * {@code PUT  /commands} : Updates an existing command.
     *
     * @param command the command to update.
     * @param id the id of the command to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated command,
     * or with status {@code 400 (Bad Request)} if the command is not valid,
     * or with status {@code 500 (Internal Server Error)} if the command couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws Exception if the the file upload process failed.
     */
    @PutMapping("/commands/{id}")
    public ResponseEntity<Command> updateCommand(@Valid @RequestBody Command command, @PathVariable("id") Long id) throws URISyntaxException, Exception {
        logger.debug("REST request to update Command : {}", command);
        if (command.getId() == null) {
            throw new BadRequestAlertException("Invalid id", "Command", "idnull");
        }
        Command result = commandService.update(command, id);
        return ResponseEntity.ok()
                .body(result);
    }

    /**
     * {@code GET  /commands} : get all the commands.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commands in body.
     */
    @GetMapping("/commands")
    public ResponseEntity<Page<Command>> getAllCommands(Pageable pageable) {
        logger.debug("REST request to get a page of Commands");
        Page<Command> page = commandService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * {@code GET  /commands/:id} : get the "id" command.
     *
     * @param id the id of the command to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the command, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/commands/{id}")
    public ResponseEntity<Command> getCommand(@PathVariable Long id) {
        logger.debug("REST request to get Command : {}", id);
        Optional<Command> command = commandService.findOne(id);
        return ResponseEntity.of(command);
    }

    /**
     * {@code DELETE  /commands/:id} : delete the "id" command.
     *
     * @param id the id of the command to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     * @throws Exception if the id of the command is incorrect.
     */
    @DeleteMapping("/commands/{id}")
    public ResponseEntity<Void> deleteCommand(@PathVariable Long id) throws Exception {
        logger.debug("REST request to delete Command : {}", id);
        commandService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@code GET  /users/{id}/commands} : get all the commands for an user.
     *
     * @param id the id of the concerned user.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commands in body.
     */
    @GetMapping("/users/{id}/commands")
    public ResponseEntity<List<Command>> getAllCommandsForUser(@PathVariable Long id) {
        logger.debug("REST request to get a page of Commands");
        /*List<Command> page = commandService.findAllByUserId(id);*/
       /* return new ResponseEntity<>(page, HttpStatus.OK);*/
        return null;

    }

   /* @GetMapping("/commands/_searchFilter")
    public ResponseEntity<Page<Command>> searchCommand(String code, @RequestParam(defaultValue = "0") int status, String lastName, String firstName, Pageable pageable) {
        logger.debug("REST request to get User By current Search");
        Page<Command> page = commandService.searchByCommandParameter(code, status, lastName, firstName, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
*/
    /**
     * {@code GET  /commands/_search} : search for the users.
     *
     * @param pageable the pagination information.
     * @param search the search criteria.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of users in body.
     */
    @GetMapping("/_search")
    public ResponseEntity<Page<Command>> _search(@RequestParam(value = "search", required = false) String search, Pageable pageable) {
        logger.debug("REST request to get a page of Users");
        CustomSpecificationBuilder<Command> builder = new CustomSpecificationBuilder<Command>();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>|!)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<Command> spec = builder.build();
        Page<Command> page = commandService.findAll(spec, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}

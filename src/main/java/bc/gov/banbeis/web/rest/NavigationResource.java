package bc.gov.banbeis.web.rest;

import bc.gov.banbeis.domain.Navigation;
import bc.gov.banbeis.repository.NavigationRepository;
import bc.gov.banbeis.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link bc.gov.banbeis.domain.Navigation}.
 */
@RestController
@RequestMapping("/api")
public class NavigationResource {

    private final Logger log = LoggerFactory.getLogger(NavigationResource.class);

    private static final String ENTITY_NAME = "navigation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NavigationRepository navigationRepository;

    public NavigationResource(NavigationRepository navigationRepository) {
        this.navigationRepository = navigationRepository;
    }

    /**
     * {@code POST  /navigations} : Create a new navigation.
     *
     * @param navigation the navigation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new navigation, or with status {@code 400 (Bad Request)} if the navigation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/navigations")
    public ResponseEntity<Navigation> createNavigation(@Valid @RequestBody Navigation navigation) throws URISyntaxException {
        log.debug("REST request to save Navigation : {}", navigation);
        if (navigation.getId() != null) {
            throw new BadRequestAlertException("A new navigation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Navigation result = navigationRepository.save(navigation);
        return ResponseEntity
            .created(new URI("/api/navigations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /navigations/:id} : Updates an existing navigation.
     *
     * @param id the id of the navigation to save.
     * @param navigation the navigation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated navigation,
     * or with status {@code 400 (Bad Request)} if the navigation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the navigation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/navigations/{id}")
    public ResponseEntity<Navigation> updateNavigation(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Navigation navigation
    ) throws URISyntaxException {
        log.debug("REST request to update Navigation : {}, {}", id, navigation);
        if (navigation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, navigation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!navigationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Navigation result = navigationRepository.save(navigation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, navigation.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /navigations/:id} : Partial updates given fields of an existing navigation, field will ignore if it is null
     *
     * @param id the id of the navigation to save.
     * @param navigation the navigation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated navigation,
     * or with status {@code 400 (Bad Request)} if the navigation is not valid,
     * or with status {@code 404 (Not Found)} if the navigation is not found,
     * or with status {@code 500 (Internal Server Error)} if the navigation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/navigations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Navigation> partialUpdateNavigation(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Navigation navigation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Navigation partially : {}, {}", id, navigation);
        if (navigation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, navigation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!navigationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Navigation> result = navigationRepository
            .findById(navigation.getId())
            .map(existingNavigation -> {
                if (navigation.getTitle() != null) {
                    existingNavigation.setTitle(navigation.getTitle());
                }
                if (navigation.getIcon() != null) {
                    existingNavigation.setIcon(navigation.getIcon());
                }
                if (navigation.getRoute() != null) {
                    existingNavigation.setRoute(navigation.getRoute());
                }
                if (navigation.getRoles() != null) {
                    existingNavigation.setRoles(navigation.getRoles());
                }

                return existingNavigation;
            })
            .map(navigationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, navigation.getId())
        );
    }

    /**
     * {@code GET  /navigations} : get all the navigations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of navigations in body.
     */
    @GetMapping("/navigations")
    public List<Navigation> getAllNavigations() {
        log.debug("REST request to get all Navigations");
        return navigationRepository.findAll();
    }

    /**
     * {@code GET  /navigations/:id} : get the "id" navigation.
     *
     * @param id the id of the navigation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the navigation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/navigations/{id}")
    public ResponseEntity<Navigation> getNavigation(@PathVariable String id) {
        log.debug("REST request to get Navigation : {}", id);
        Optional<Navigation> navigation = navigationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(navigation);
    }

    /**
     * {@code DELETE  /navigations/:id} : delete the "id" navigation.
     *
     * @param id the id of the navigation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/navigations/{id}")
    public ResponseEntity<Void> deleteNavigation(@PathVariable String id) {
        log.debug("REST request to delete Navigation : {}", id);
        navigationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}

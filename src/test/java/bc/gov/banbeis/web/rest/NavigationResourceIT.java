package bc.gov.banbeis.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import bc.gov.banbeis.IntegrationTest;
import bc.gov.banbeis.domain.Navigation;
import bc.gov.banbeis.repository.NavigationRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link NavigationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NavigationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_ROUTE = "AAAAAAAAAA";
    private static final String UPDATED_ROUTE = "BBBBBBBBBB";

    private static final String DEFAULT_ROLES = "AAAAAAAAAA";
    private static final String UPDATED_ROLES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/navigations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private NavigationRepository navigationRepository;

    @Autowired
    private MockMvc restNavigationMockMvc;

    private Navigation navigation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Navigation createEntity() {
        Navigation navigation = new Navigation().title(DEFAULT_TITLE).icon(DEFAULT_ICON).route(DEFAULT_ROUTE).roles(DEFAULT_ROLES);
        return navigation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Navigation createUpdatedEntity() {
        Navigation navigation = new Navigation().title(UPDATED_TITLE).icon(UPDATED_ICON).route(UPDATED_ROUTE).roles(UPDATED_ROLES);
        return navigation;
    }

    @BeforeEach
    public void initTest() {
        navigationRepository.deleteAll();
        navigation = createEntity();
    }

    @Test
    void createNavigation() throws Exception {
        int databaseSizeBeforeCreate = navigationRepository.findAll().size();
        // Create the Navigation
        restNavigationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(navigation)))
            .andExpect(status().isCreated());

        // Validate the Navigation in the database
        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeCreate + 1);
        Navigation testNavigation = navigationList.get(navigationList.size() - 1);
        assertThat(testNavigation.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNavigation.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testNavigation.getRoute()).isEqualTo(DEFAULT_ROUTE);
        assertThat(testNavigation.getRoles()).isEqualTo(DEFAULT_ROLES);
    }

    @Test
    void createNavigationWithExistingId() throws Exception {
        // Create the Navigation with an existing ID
        navigation.setId("existing_id");

        int databaseSizeBeforeCreate = navigationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNavigationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(navigation)))
            .andExpect(status().isBadRequest());

        // Validate the Navigation in the database
        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = navigationRepository.findAll().size();
        // set the field null
        navigation.setTitle(null);

        // Create the Navigation, which fails.

        restNavigationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(navigation)))
            .andExpect(status().isBadRequest());

        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIconIsRequired() throws Exception {
        int databaseSizeBeforeTest = navigationRepository.findAll().size();
        // set the field null
        navigation.setIcon(null);

        // Create the Navigation, which fails.

        restNavigationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(navigation)))
            .andExpect(status().isBadRequest());

        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllNavigations() throws Exception {
        // Initialize the database
        navigationRepository.save(navigation);

        // Get all the navigationList
        restNavigationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(navigation.getId())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].route").value(hasItem(DEFAULT_ROUTE)))
            .andExpect(jsonPath("$.[*].roles").value(hasItem(DEFAULT_ROLES)));
    }

    @Test
    void getNavigation() throws Exception {
        // Initialize the database
        navigationRepository.save(navigation);

        // Get the navigation
        restNavigationMockMvc
            .perform(get(ENTITY_API_URL_ID, navigation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(navigation.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.route").value(DEFAULT_ROUTE))
            .andExpect(jsonPath("$.roles").value(DEFAULT_ROLES));
    }

    @Test
    void getNonExistingNavigation() throws Exception {
        // Get the navigation
        restNavigationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putNewNavigation() throws Exception {
        // Initialize the database
        navigationRepository.save(navigation);

        int databaseSizeBeforeUpdate = navigationRepository.findAll().size();

        // Update the navigation
        Navigation updatedNavigation = navigationRepository.findById(navigation.getId()).get();
        updatedNavigation.title(UPDATED_TITLE).icon(UPDATED_ICON).route(UPDATED_ROUTE).roles(UPDATED_ROLES);

        restNavigationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNavigation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNavigation))
            )
            .andExpect(status().isOk());

        // Validate the Navigation in the database
        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeUpdate);
        Navigation testNavigation = navigationList.get(navigationList.size() - 1);
        assertThat(testNavigation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNavigation.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testNavigation.getRoute()).isEqualTo(UPDATED_ROUTE);
        assertThat(testNavigation.getRoles()).isEqualTo(UPDATED_ROLES);
    }

    @Test
    void putNonExistingNavigation() throws Exception {
        int databaseSizeBeforeUpdate = navigationRepository.findAll().size();
        navigation.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNavigationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, navigation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(navigation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Navigation in the database
        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNavigation() throws Exception {
        int databaseSizeBeforeUpdate = navigationRepository.findAll().size();
        navigation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNavigationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(navigation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Navigation in the database
        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNavigation() throws Exception {
        int databaseSizeBeforeUpdate = navigationRepository.findAll().size();
        navigation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNavigationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(navigation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Navigation in the database
        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNavigationWithPatch() throws Exception {
        // Initialize the database
        navigationRepository.save(navigation);

        int databaseSizeBeforeUpdate = navigationRepository.findAll().size();

        // Update the navigation using partial update
        Navigation partialUpdatedNavigation = new Navigation();
        partialUpdatedNavigation.setId(navigation.getId());

        partialUpdatedNavigation.title(UPDATED_TITLE).roles(UPDATED_ROLES);

        restNavigationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNavigation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNavigation))
            )
            .andExpect(status().isOk());

        // Validate the Navigation in the database
        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeUpdate);
        Navigation testNavigation = navigationList.get(navigationList.size() - 1);
        assertThat(testNavigation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNavigation.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testNavigation.getRoute()).isEqualTo(DEFAULT_ROUTE);
        assertThat(testNavigation.getRoles()).isEqualTo(UPDATED_ROLES);
    }

    @Test
    void fullUpdateNavigationWithPatch() throws Exception {
        // Initialize the database
        navigationRepository.save(navigation);

        int databaseSizeBeforeUpdate = navigationRepository.findAll().size();

        // Update the navigation using partial update
        Navigation partialUpdatedNavigation = new Navigation();
        partialUpdatedNavigation.setId(navigation.getId());

        partialUpdatedNavigation.title(UPDATED_TITLE).icon(UPDATED_ICON).route(UPDATED_ROUTE).roles(UPDATED_ROLES);

        restNavigationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNavigation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNavigation))
            )
            .andExpect(status().isOk());

        // Validate the Navigation in the database
        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeUpdate);
        Navigation testNavigation = navigationList.get(navigationList.size() - 1);
        assertThat(testNavigation.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNavigation.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testNavigation.getRoute()).isEqualTo(UPDATED_ROUTE);
        assertThat(testNavigation.getRoles()).isEqualTo(UPDATED_ROLES);
    }

    @Test
    void patchNonExistingNavigation() throws Exception {
        int databaseSizeBeforeUpdate = navigationRepository.findAll().size();
        navigation.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNavigationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, navigation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(navigation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Navigation in the database
        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNavigation() throws Exception {
        int databaseSizeBeforeUpdate = navigationRepository.findAll().size();
        navigation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNavigationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(navigation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Navigation in the database
        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNavigation() throws Exception {
        int databaseSizeBeforeUpdate = navigationRepository.findAll().size();
        navigation.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNavigationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(navigation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Navigation in the database
        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNavigation() throws Exception {
        // Initialize the database
        navigationRepository.save(navigation);

        int databaseSizeBeforeDelete = navigationRepository.findAll().size();

        // Delete the navigation
        restNavigationMockMvc
            .perform(delete(ENTITY_API_URL_ID, navigation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Navigation> navigationList = navigationRepository.findAll();
        assertThat(navigationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package bc.gov.banbeis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A SubNavigation.
 */
public class SubNavigation implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Field("title")
    private String title;

    @NotNull
    @Field("icon")
    private String icon;

    @Field("route")
    private String route;

    @Field("roles")
    private String roles;

    @DBRef
    @Field("navigation")
    @JsonIgnoreProperties(value = { "submenus" }, allowSetters = true)
    private Navigation navigation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getTitle() {
        return this.title;
    }

    public SubNavigation title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return this.icon;
    }

    public SubNavigation icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRoute() {
        return this.route;
    }

    public SubNavigation route(String route) {
        this.setRoute(route);
        return this;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRoles() {
        return this.roles;
    }

    public SubNavigation roles(String roles) {
        this.setRoles(roles);
        return this;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Navigation getNavigation() {
        return this.navigation;
    }

    public void setNavigation(Navigation navigation) {
        this.navigation = navigation;
    }

    public SubNavigation navigation(Navigation navigation) {
        this.setNavigation(navigation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubNavigation)) {
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubNavigation{" +
            ", title='" + getTitle() + "'" +
            ", icon='" + getIcon() + "'" +
            ", route='" + getRoute() + "'" +
            ", roles='" + getRoles() + "'" +
            "}";
    }
}

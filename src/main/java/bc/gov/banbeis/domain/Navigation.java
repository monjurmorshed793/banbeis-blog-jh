package bc.gov.banbeis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Navigation.
 */
@Document(collection = "navigation")
public class Navigation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

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

    @Field("submenus")
    @JsonIgnoreProperties(value = { "navigation" }, allowSetters = true)
    private Set<SubNavigation> submenus = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Navigation id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Navigation title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return this.icon;
    }

    public Navigation icon(String icon) {
        this.setIcon(icon);
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRoute() {
        return this.route;
    }

    public Navigation route(String route) {
        this.setRoute(route);
        return this;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRoles() {
        return this.roles;
    }

    public Navigation roles(String roles) {
        this.setRoles(roles);
        return this;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Set<SubNavigation> getSubmenus() {
        return this.submenus;
    }

    public void setSubmenus(Set<SubNavigation> subNavigations) {
        if (this.submenus != null) {
            this.submenus.forEach(i -> i.setNavigation(null));
        }
        if (subNavigations != null) {
            subNavigations.forEach(i -> i.setNavigation(this));
        }
        this.submenus = subNavigations;
    }

    public Navigation submenus(Set<SubNavigation> subNavigations) {
        this.setSubmenus(subNavigations);
        return this;
    }

    public Navigation addSubmenus(SubNavigation subNavigation) {
        this.submenus.add(subNavigation);
        return this;
    }

    public Navigation removeSubmenus(SubNavigation subNavigation) {
        this.submenus.remove(subNavigation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Navigation)) {
            return false;
        }
        return id != null && id.equals(((Navigation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Navigation{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", icon='" + getIcon() + "'" +
            ", route='" + getRoute() + "'" +
            ", roles='" + getRoles() + "'" +
            "}";
    }
}

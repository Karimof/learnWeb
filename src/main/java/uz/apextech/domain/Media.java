package uz.apextech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Media.
 */
@Entity
@Table(name = "media")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 1000)
    @Column(name = "title", length = 1000)
    private String title;

    @Size(max = 3000)
    @Column(name = "description", length = 3000)
    private String description;

    @Size(max = 512)
    @Column(name = "photo", length = 512)
    private String photo;

    @Size(max = 512)
    @Column(name = "video", length = 512)
    private String video;

    @ManyToOne
    @JsonIgnoreProperties(value = { "course" }, allowSetters = true)
    private Theme theme;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Media id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Media title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Media description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return this.photo;
    }

    public Media photo(String photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getVideo() {
        return this.video;
    }

    public Media video(String video) {
        this.setVideo(video);
        return this;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Theme getTheme() {
        return this.theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Media theme(Theme theme) {
        this.setTheme(theme);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Media)) {
            return false;
        }
        return id != null && id.equals(((Media) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Media{" +
            "id=" +
            id +
            ", title='" +
            title +
            '\'' +
            ", description='" +
            description +
            '\'' +
            ", photo='" +
            photo +
            '\'' +
            ", video='" +
            video +
            '\'' +
            ", theme=" +
            theme +
            '}'
        );
    }
}

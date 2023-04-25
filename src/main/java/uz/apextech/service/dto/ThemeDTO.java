package uz.apextech.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link uz.apextech.domain.Theme} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ThemeDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    private CourseDTO course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThemeDTO)) {
            return false;
        }

        ThemeDTO themeDTO = (ThemeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, themeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThemeDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", course=" + getCourse() +
            "}";
    }
}

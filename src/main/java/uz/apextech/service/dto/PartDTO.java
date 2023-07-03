package uz.apextech.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link uz.apextech.domain.Part} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @Size(max = 5000)
    private String description;

    @Size(max = 1000)
    private String question;

    @Size(max = 1000)
    private String codeTitle;

    @Size(max = 3000)
    private String codeDescription;

    @Size(max = 3000)
    private String code;

    @Size(max = 5000)
    private String fullCode;

    @Size(max = 1000)
    private String result;

    @Size(max = 3000)
    private String additional;

    private ThemeDTO theme;

    private MediaDTO media;

    private Long submenuId;

    private Long subSubmenuId;

    private Long belongToSubsub;
    private String presentation;
    private String test;
    private String crossword;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCodeTitle() {
        return codeTitle;
    }

    public void setCodeTitle(String codeTitle) {
        this.codeTitle = codeTitle;
    }

    public String getCodeDescription() {
        return codeDescription;
    }

    public void setCodeDescription(String codeDescription) {
        this.codeDescription = codeDescription;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullCode() {
        return fullCode;
    }

    public void setFullCode(String fullCode) {
        this.fullCode = fullCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public ThemeDTO getTheme() {
        return theme;
    }

    public void setTheme(ThemeDTO theme) {
        this.theme = theme;
    }

    public MediaDTO getMedia() {
        return media;
    }

    public void setMedia(MediaDTO media) {
        this.media = media;
    }

    public Long getSubmenuId() {
        return submenuId;
    }

    public void setSubmenuId(Long submenuId) {
        this.submenuId = submenuId;
    }

    public Long getSubSubmenuId() {
        return subSubmenuId;
    }

    public void setSubSubmenuId(Long subSubmenuId) {
        this.subSubmenuId = subSubmenuId;
    }

    public Long getBelongToSubsub() {
        return belongToSubsub;
    }

    public void setBelongToSubsub(Long belongToSubsub) {
        this.belongToSubsub = belongToSubsub;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getCrossword() {
        return crossword;
    }

    public void setCrossword(String crossword) {
        this.crossword = crossword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PartDTO)) {
            return false;
        }

        PartDTO partDTO = (PartDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, partDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", question='" + getQuestion() + "'" +
            ", codeTitle='" + getCodeTitle() + "'" +
            ", codeDescription='" + getCodeDescription() + "'" +
            ", code='" + getCode() + "'" +
            ", fullCode='" + getFullCode() + "'" +
            ", result='" + getResult() + "'" +
            ", additional='" + getAdditional() + "'" +
            ", submenuId='" + getSubmenuId() + "'" +
            ", subSubmenuId='" + getSubSubmenuId() + "'" +
            ", belongToSubsub='" + getBelongToSubsub() + "'" +
            ", presentation='" + getPresentation() + "'" +
            ", test='" + getTest() + "'" +
            ", crossword='" + getCrossword() + "'" +
            ", theme=" + getTheme() +
            ", media=" + getMedia() +
            "}";
    }
}

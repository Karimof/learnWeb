package uz.apextech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Part.
 */
@Entity
@Table(name = "part")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Part implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 5000)
    @Column(name = "description", length = 5000)
    private String description;

    @Size(max = 1000)
    @Column(name = "question", length = 1000)
    private String question;

    @Size(max = 1000)
    @Column(name = "code_title", length = 1000)
    private String codeTitle;

    @Size(max = 3000)
    @Column(name = "code_description", length = 3000)
    private String codeDescription;

    @Size(max = 3000)
    @Column(name = "code", length = 3000)
    private String code;

    @Size(max = 5000)
    @Column(name = "full_code", length = 5000)
    private String fullCode;

    @Size(max = 1000)
    @Column(name = "result", length = 1000)
    private String result;

    @Size(max = 3000)
    @Column(name = "additional", length = 3000)
    private String additional;

    @Column(name = "submenuId")
    private Long submenuId;

    @Column(name = "sub_submenu_id")
    private Long subSubmenuId;

    @Column(name = "belong_to_subsub")
    private Long belongToSubsub;

    @Column(name = "presentation")
    private String presentation;

    @Column(name = "test")
    private String test;

    @Column(name = "crossword")
    private String crossword;

    @ManyToOne
    private Theme theme;

    @ManyToOne
    private Media media;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Part id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Part title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Part description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestion() {
        return this.question;
    }

    public Part question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCodeTitle() {
        return this.codeTitle;
    }

    public Part codeTitle(String codeTitle) {
        this.setCodeTitle(codeTitle);
        return this;
    }

    public void setCodeTitle(String codeTitle) {
        this.codeTitle = codeTitle;
    }

    public String getCodeDescription() {
        return this.codeDescription;
    }

    public Part codeDescription(String codeDescription) {
        this.setCodeDescription(codeDescription);
        return this;
    }

    public void setCodeDescription(String codeDescription) {
        this.codeDescription = codeDescription;
    }

    public String getCode() {
        return this.code;
    }

    public Part code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullCode() {
        return this.fullCode;
    }

    public Part fullCode(String fullCode) {
        this.setFullCode(fullCode);
        return this;
    }

    public void setFullCode(String fullCode) {
        this.fullCode = fullCode;
    }

    public String getResult() {
        return this.result;
    }

    public Part result(String result) {
        this.setResult(result);
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAdditional() {
        return this.additional;
    }

    public Part additional(String additional) {
        this.setAdditional(additional);
        return this;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public Theme getTheme() {
        return this.theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Part theme(Theme theme) {
        this.setTheme(theme);
        return this;
    }

    public Media getMedia() {
        return this.media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Part media(Media media) {
        this.setMedia(media);
        return this;
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

    public void setBelongToSubsub(Long belongToSubsub) {
        this.belongToSubsub = belongToSubsub;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Part)) {
            return false;
        }
        return id != null && id.equals(((Part) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Part{" +
            "id=" +
            id +
            ", title='" +
            title +
            '\'' +
            ", description='" +
            description +
            '\'' +
            ", question='" +
            question +
            '\'' +
            ", codeTitle='" +
            codeTitle +
            '\'' +
            ", codeDescription='" +
            codeDescription +
            '\'' +
            ", code='" +
            code +
            '\'' +
            ", fullCode='" +
            fullCode +
            '\'' +
            ", result='" +
            result +
            '\'' +
            ", additional='" +
            additional +
            '\'' +
            ", submenuId=" +
            submenuId +
            ", subSubmenuId=" +
            subSubmenuId +
            ", belongToSubsub=" +
            belongToSubsub +
            ", presentation='" +
            presentation +
            '\'' +
            ", test='" +
            test +
            '\'' +
            ", crossword='" +
            crossword +
            '\'' +
            ", theme=" +
            theme +
            ", media=" +
            media +
            '}'
        );
    }
}

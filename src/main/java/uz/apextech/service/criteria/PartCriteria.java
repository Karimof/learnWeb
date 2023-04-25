package uz.apextech.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link uz.apextech.domain.Part} entity. This class is used
 * in {@link uz.apextech.web.rest.PartResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PartCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private StringFilter question;

    private StringFilter codeTitle;

    private StringFilter codeDescription;

    private StringFilter code;

    private StringFilter fullCode;

    private StringFilter result;

    private StringFilter additional;

    private LongFilter themeId;

    private LongFilter mediaId;

    private Boolean distinct;

    public PartCriteria() {}

    public PartCriteria(PartCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.question = other.question == null ? null : other.question.copy();
        this.codeTitle = other.codeTitle == null ? null : other.codeTitle.copy();
        this.codeDescription = other.codeDescription == null ? null : other.codeDescription.copy();
        this.code = other.code == null ? null : other.code.copy();
        this.fullCode = other.fullCode == null ? null : other.fullCode.copy();
        this.result = other.result == null ? null : other.result.copy();
        this.additional = other.additional == null ? null : other.additional.copy();
        this.themeId = other.themeId == null ? null : other.themeId.copy();
        this.mediaId = other.mediaId == null ? null : other.mediaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PartCriteria copy() {
        return new PartCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getQuestion() {
        return question;
    }

    public StringFilter question() {
        if (question == null) {
            question = new StringFilter();
        }
        return question;
    }

    public void setQuestion(StringFilter question) {
        this.question = question;
    }

    public StringFilter getCodeTitle() {
        return codeTitle;
    }

    public StringFilter codeTitle() {
        if (codeTitle == null) {
            codeTitle = new StringFilter();
        }
        return codeTitle;
    }

    public void setCodeTitle(StringFilter codeTitle) {
        this.codeTitle = codeTitle;
    }

    public StringFilter getCodeDescription() {
        return codeDescription;
    }

    public StringFilter codeDescription() {
        if (codeDescription == null) {
            codeDescription = new StringFilter();
        }
        return codeDescription;
    }

    public void setCodeDescription(StringFilter codeDescription) {
        this.codeDescription = codeDescription;
    }

    public StringFilter getCode() {
        return code;
    }

    public StringFilter code() {
        if (code == null) {
            code = new StringFilter();
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getFullCode() {
        return fullCode;
    }

    public StringFilter fullCode() {
        if (fullCode == null) {
            fullCode = new StringFilter();
        }
        return fullCode;
    }

    public void setFullCode(StringFilter fullCode) {
        this.fullCode = fullCode;
    }

    public StringFilter getResult() {
        return result;
    }

    public StringFilter result() {
        if (result == null) {
            result = new StringFilter();
        }
        return result;
    }

    public void setResult(StringFilter result) {
        this.result = result;
    }

    public StringFilter getAdditional() {
        return additional;
    }

    public StringFilter additional() {
        if (additional == null) {
            additional = new StringFilter();
        }
        return additional;
    }

    public void setAdditional(StringFilter additional) {
        this.additional = additional;
    }

    public LongFilter getThemeId() {
        return themeId;
    }

    public LongFilter themeId() {
        if (themeId == null) {
            themeId = new LongFilter();
        }
        return themeId;
    }

    public void setThemeId(LongFilter themeId) {
        this.themeId = themeId;
    }

    public LongFilter getMediaId() {
        return mediaId;
    }

    public LongFilter mediaId() {
        if (mediaId == null) {
            mediaId = new LongFilter();
        }
        return mediaId;
    }

    public void setMediaId(LongFilter mediaId) {
        this.mediaId = mediaId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PartCriteria that = (PartCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(question, that.question) &&
            Objects.equals(codeTitle, that.codeTitle) &&
            Objects.equals(codeDescription, that.codeDescription) &&
            Objects.equals(code, that.code) &&
            Objects.equals(fullCode, that.fullCode) &&
            Objects.equals(result, that.result) &&
            Objects.equals(additional, that.additional) &&
            Objects.equals(themeId, that.themeId) &&
            Objects.equals(mediaId, that.mediaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            title,
            description,
            question,
            codeTitle,
            codeDescription,
            code,
            fullCode,
            result,
            additional,
            themeId,
            mediaId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PartCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (question != null ? "question=" + question + ", " : "") +
            (codeTitle != null ? "codeTitle=" + codeTitle + ", " : "") +
            (codeDescription != null ? "codeDescription=" + codeDescription + ", " : "") +
            (code != null ? "code=" + code + ", " : "") +
            (fullCode != null ? "fullCode=" + fullCode + ", " : "") +
            (result != null ? "result=" + result + ", " : "") +
            (additional != null ? "additional=" + additional + ", " : "") +
            (themeId != null ? "themeId=" + themeId + ", " : "") +
            (mediaId != null ? "mediaId=" + mediaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}

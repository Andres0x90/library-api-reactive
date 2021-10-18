package co.com.sofka.libraryapireactive.dtos;

public class ResourceDTO {

    private String id;
    private String title;
    private String type;
    private String subjectArea;
    private boolean isLent;
    private String dateLent;

    public ResourceDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubjectArea() {
        return subjectArea;
    }

    public void setSubjectArea(String subjectArea) {
        this.subjectArea = subjectArea;
    }

    public boolean isLent() {
        return isLent;
    }

    public void setLent(boolean lent) {
        isLent = lent;
    }

    public String getDateLent() {
        return dateLent;
    }

    public void setDateLent(String dateLent) {
        this.dateLent = dateLent;
    }
}
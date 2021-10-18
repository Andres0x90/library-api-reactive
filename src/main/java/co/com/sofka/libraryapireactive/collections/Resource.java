package co.com.sofka.libraryapireactive.collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

import java.time.LocalDate;

@Document
public class Resource {
    @Id
    private String id;
    private String title;
    private String type;
    private String subjectArea;
    private boolean isLent;
    private LocalDate dateLent;

    public Resource() {
        this.id = UUID.randomUUID().toString();
        this.isLent = false;
    }
    public void lend()
    {
        this.isLent = true;
        this.dateLent = LocalDate.now();
    }
    public void giveBack()
    {
        this.isLent = false;
        this.dateLent = null;
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

    public LocalDate getDateLent() {
        return dateLent;
    }

    public void setDateLent(LocalDate dateLent) {
        this.dateLent = dateLent;
    }
}
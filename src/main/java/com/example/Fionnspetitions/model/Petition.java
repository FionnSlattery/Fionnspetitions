package com.example.Fionnspetitions.model;


import java.util.ArrayList;
import java.util.List;

public class Petition {
    private long id;
    private String title;
    private String description;
    private String createdBy;
    private List<Signature> signatures = new ArrayList<>();

    public Petition() {}

    public Petition(long id, String title, String description, String createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public List<Signature> getSignatures() { return signatures; }
    public void setSignatures(List<Signature> signatures) { this.signatures = signatures; }

    public void addSignature(Signature s) {
        this.signatures.add(s);
    }
}


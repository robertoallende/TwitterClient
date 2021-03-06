package com.robertoallende.twitterclient.entities;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "TWEET".
 */
public class Tweet {

    private Long localId;
    private Long serverId;
    private String text;
    private Long userId;
    private Boolean isLocal;
    private java.util.Date createdAt;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Tweet() {
    }

    public Tweet(Long localId) {
        this.localId = localId;
    }

    public Tweet(Long localId, Long serverId, String text, Long userId, Boolean isLocal, java.util.Date createdAt) {
        this.localId = localId;
        this.serverId = serverId;
        this.text = text;
        this.userId = userId;
        this.isLocal = isLocal;
        this.createdAt = createdAt;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(Boolean isLocal) {
        this.isLocal = isLocal;
    }

    public java.util.Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.util.Date createdAt) {
        this.createdAt = createdAt;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}

package org.frisch;

import com.fasterxml.jackson.annotation.JsonView;

public class Example {

    @JsonView({JsonExample.class})
    private String text;

    @JsonView({JsonExample.class})
    private Integer voice;
    @JsonView({JsonExample.class})
    private String language;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getVoice() {
        return voice;
    }

    public void setVoice(Integer voice) {
        this.voice = voice;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

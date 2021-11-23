package main.DTOs;

import java.util.ArrayList;
import java.util.List;

public class DTOStories {
    private List<StoryPrincipal> stories;

    public DTOStories() {
        this.stories = new ArrayList<>();
    }

    public List<StoryPrincipal> getStories() {
        return stories;
    }

    public void setStories(List<StoryPrincipal> stories) {
        this.stories = stories;
    }
}

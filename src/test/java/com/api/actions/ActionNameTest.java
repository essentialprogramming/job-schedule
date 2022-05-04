package com.api.actions;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ActionNameTest {

    @Test
    void get_next_action(){
        assertThat(ActionType.ASSIGN_STORY.getNextAction()).isEqualTo(ActionType.IMPLEMENT_REQUIREMENTS);
        assertThat(ActionType.IMPLEMENT_REQUIREMENTS.getNextAction()).isEqualTo(ActionType.SEND_PULL_REQUEST_EVENT);
        assertThat(ActionType.SEND_PULL_REQUEST_EVENT.getNextAction()).isEqualTo(ActionType.REVIEW_PULL_REQUEST_EVENT);
        assertThat(ActionType.REVIEW_PULL_REQUEST_EVENT.getNextAction()).isEqualTo(ActionType.MERGE_CHANGES);
        assertThat(ActionType.MERGE_CHANGES.getNextAction()).isEqualTo(ActionType.SEND_STORY_COMPLETE_NOTIFICATION);

        assertThat(ActionType.SEND_STORY_COMPLETE_NOTIFICATION.getNextAction()).isNull();
    }
}

package ru.golovkov.taskstn.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class UserSuggestionResponseDto {

    @JsonProperty("USER")
    private List<UserSuggestionItem> userSuggestionItemList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserSuggestionItem {

        private String valueId;
        private String value;
        private String directoryType;
        private Object entityFields;
    }
}

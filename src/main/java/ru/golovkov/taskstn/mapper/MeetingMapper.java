package ru.golovkov.taskstn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.golovkov.taskstn.model.dto.request.MeetingRequestDto;
import ru.golovkov.taskstn.model.dto.response.MeetingResponseDto;
import ru.golovkov.taskstn.model.entity.Meeting;
import ru.golovkov.taskstn.model.entity.User;

import java.util.List;

@Mapper
public interface MeetingMapper {

    @Mapping(target = "id", ignore = true)
    Meeting toEntity(MeetingRequestDto requestDto);

    @Mapping(target = "applicantId", source = "applicant.id")
    @Mapping(target = "authorEmail", source = "author.email")
    @Mapping(target = "recipientEmails", source = "recipients", qualifiedByName = "mapRecipientsToEmails")
    MeetingResponseDto toResponseDto(Meeting meeting);

    List<MeetingResponseDto> toResponseDtoList(List<Meeting> meetingList);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequestDto(@MappingTarget Meeting meeting, MeetingRequestDto requestDto);

    @Named("mapRecipientsToEmails")
    default List<String> mapRecipientsToEmails(List<User> recipients) {
        return recipients.stream()
                .map(User::getEmail)
                .toList();
    }
}

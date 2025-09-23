package com.openclassrooms.noteservice.mapper;


import com.openclassrooms.noteservice.dto.NoteDto;
import com.openclassrooms.noteservice.entity.NoteEntity;
import org.mapstruct.Mapper;




@Mapper(componentModel = "spring")
public interface NoteMapper {

    NoteEntity mapToNoteEntity(NoteDto noteDto);

    NoteDto mapToNoteDto(NoteEntity noteEntity);


}

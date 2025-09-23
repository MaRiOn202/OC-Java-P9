package com.openclassrooms.noteservice.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(collection = "notes")
public class NoteEntity {

    @Id
    private String id;    // MongoDb = ObjectId

    private Long patientId;

    private String contenuNote;

    private LocalDateTime dateCreation;





}

package com.example.iustvoicespeech.model;

import com.example.iustvoicespeech.database.UuidConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.UUID;

@Entity
public class Text {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "text")
    private String text;

    @Property(nameInDb = "text_uuid")
    @Index(unique = true)
    @Convert(converter = UuidConverter.class, columnType = String.class)
    private UUID textUUId;


    public Text( String text) {
        this.text = text;
        this.textUUId = UUID.randomUUID();
    }

    public String getText() {
        return text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getTextUUId() {
        return textUUId;
    }

}

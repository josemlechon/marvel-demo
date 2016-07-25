package com.marvel.demo.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marvel.demo.domain.model.AppDatabase;
import com.marvel.demo.domain.model.Validation;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jose m lechon on 20/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
@ModelContainer
@Table(database = AppDatabase.class)
public class Image extends BaseModel implements Validation {

    @Column
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    @JsonProperty("path")
    public String path;

    @Column
    @JsonProperty("extension")
    public String extension;

    public String getImageURL() {
        return path + "." + extension;
    }

    public void setComic(Comic comic) {

        this.comicContainer = new ForeignKeyContainer<>(Comic.class);
        this.comicContainer.setModel(comic);
        Map<String,Object> values = new LinkedHashMap<>();
        values.put("idComic",comic.idComic);
        this.comicContainer.setData(values);
    }


    @Column
    @ForeignKey(references = {
            @ForeignKeyReference(
                    columnName = /* image */"comic_idComic",
                    columnType = Long.class,
                    foreignKeyColumnName = /* comic */"idComic")},
            saveForeignKeyModel = false)
//    @ForeignKey(tableClass=Comic.class,
//            references = {@ForeignKeyReference(
//                    columnName = "comic_idComic",
//                    columnType = Long.class,
//                    foreignKeyColumnName = "idComic")},
//            saveForeignKeyModel = false , onDelete = ForeignKeyAction.CASCADE)

    public ForeignKeyContainer<Comic> comicContainer;


    @Override
    public void validate() {

    }
}

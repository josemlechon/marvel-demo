package com.marvel.demo.domain.entities;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marvel.demo.domain.errors.ValidationFailedException;
import com.marvel.demo.domain.model.AppDatabase;
import com.marvel.demo.domain.model.Validation;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.container.ForeignKeyContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.http.Url;

/**
 * Created by jose m lechon on 19/07/16.
 *
 * @version 0.1.0
 * @since 1
 */

@Table(database = AppDatabase.class)
@ModelContainer
public class Comic extends BaseModel implements Validation {

    @Column
    @PrimaryKey(autoincrement = true)
    public Long uuid;

    @Column
    @JsonProperty("id")
    public Long idComic;

    @Column
    @JsonProperty("title")
    public String title;

    @Column
    @JsonProperty("description")
    public String description;


    @JsonProperty("thumbnail")
    private  Image thumbnail;

    @Column
    @ForeignKey
    ForeignKeyContainer<Image> thumbnailContainer;


    public void associateImage(Image pThumbnail) {
        if(thumbnail == null) return;

        pThumbnail.save();
        this.thumbnailContainer= FlowManager.getContainerAdapter(Image.class)
                .toForeignKeyContainer(pThumbnail);
    }



    public @NonNull String getURLThumbnail(){

        if(thumbnail != null) return thumbnail.getImageURL();

        if(thumbnailContainer!=null && thumbnailContainer.getData() != null ) {

            Long id = (Long) thumbnailContainer.getData().get("id");
            thumbnail = SQLite.select()
                        .from(Image.class)
                        .where(Image_Table.id.is( id) )
                        .querySingle();


            return (thumbnail!=null)? thumbnail.getImageURL() : "";
        }else {
            return "";
        }
    }



    @JsonProperty("images")
    public List<Image> images;


    @OneToMany(methods = {OneToMany.Method.ALL}) //, variableName = "images"
    public List<Image> getImages() {

        if (images == null || images.isEmpty()) {
            images = SQLite.select()
                    .from(Image.class)
                    .where(Image_Table.comic_idComic.is( idComic) )
                    .queryList();
        }
        return images;
    }



    public @NonNull Image getRandomImage(){

        if(images == null || images.isEmpty()) return  new Image();

        Random r = new Random();
        int index = r.nextInt(images.size());

        return images.get(index);
    }


    /**
     * Let's suppose that we want to have all comics with the right information to show
     * if one of the fields has not been download due to lack of information then we will discard
     * it
     */
    @Override
    public void validate() {

        if( idComic <= 0 ) throw new ValidationFailedException();

        else if ( TextUtils.isEmpty(title) )  throw new ValidationFailedException();

        else if ( TextUtils.isEmpty(description) )  throw new ValidationFailedException();

        else if ( thumbnail == null )  throw new ValidationFailedException();

        else if ( images == null || images.isEmpty() )  throw new ValidationFailedException();

    }


    @Override
    public void save() {

        associateImage(thumbnail);


        super.save();

        if(images != null && !images.isEmpty()) {
            for(Image image : images){
                image.setComic(this);
                image.save();
            }
        }


    }


    @Override
    public void update() {

        associateImage(thumbnail);

        super.update();

        if(images != null && !images.isEmpty()) {
            for(Image image : images){
                image.setComic(this);
                image.save();
            }
        }


    }
}



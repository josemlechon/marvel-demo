package com.marvel.demo.domain.model;


import com.marvel.demo.app.MarvelApplication;
import com.marvel.demo.domain.entities.Comic;
import com.marvel.demo.domain.entities.Comic_Table;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.List;

/**
 * Created by jose m lechon on 23/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class ComicModel extends BaseModel {


    static final String TAG = ComicModel.class.getSimpleName();


    ImageModel mImageModel;

    public ComicModel(MarvelApplication app, DatabaseWrapper database, ImageModel imageModel  )  {
        super(app, database);

        this.mImageModel = imageModel;
    }


    public Comic getComic(Long id) {
        return new Select().from(Comic.class)
                .where(Comic_Table.idComic.eq(id))
                .querySingle();
    }

    public List<Comic> getAllComics() {

        return new Select().from(Comic.class)
                .queryList();
    }


    public synchronized void saveAll(final List<Comic> dataList) {
        pruneInvalid(dataList);
        if (dataList.isEmpty()) {
            return;
        }

        for (Comic place : dataList) {
            saveValid(place);
        }

    }


    private void saveValid(Comic data) {
        Comic stored = getComic(data.idComic);
        if (stored == null) {
            data.save();
        } else {
            data.idComic = stored.idComic;
            data.update();
        }
    }


    public void cleanTable() {
        new Delete().from(Comic.class).query();

    }

}

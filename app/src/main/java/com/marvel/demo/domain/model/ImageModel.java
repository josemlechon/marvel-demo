package com.marvel.demo.domain.model;


import com.marvel.demo.app.MarvelApplication;
import com.marvel.demo.domain.entities.Image;
import com.marvel.demo.domain.entities.Image_Table;
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
public class ImageModel extends BaseModel {


    static final String TAG = ImageModel.class.getSimpleName();


    public ImageModel(MarvelApplication app, DatabaseWrapper database) {
        super(app, database);
    }


    public Image getImage(Long id) {
        return new Select().from(Image.class)
                .where(Image_Table.id.eq(id))
                .querySingle();
    }

    public List<Image> getAllImages() {

        return new Select().from(Image.class)
                .queryList();
    }


    public synchronized void saveAll(final List<Image> dataList) {
        pruneInvalid(dataList);

        if (dataList.isEmpty()) {
            return;
        }

        for (Image place : dataList) {
            saveValid(place);
        }

    }


    private void saveValid(Image data) {
        Image stored = getImage(data.id);
        if (stored == null) {
            data.save();
        } else {
            data.id = stored.id;
            data.update();
        }
    }


    public void cleanTable() {
        new Delete().from(Image.class).query();
         
    }

}

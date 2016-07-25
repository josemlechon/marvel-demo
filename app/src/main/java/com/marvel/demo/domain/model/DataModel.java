package com.marvel.demo.domain.model;


import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.marvel.demo.app.MarvelApplication;
import com.marvel.demo.domain.entities.Data;
import com.marvel.demo.domain.entities.Data_Table;
import com.marvel.demo.domain.errors.ValidationFailedException;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by jose m lechon on 23/07/16.
 *
 * @version 0.1.0
 * @since 1
 */
public class DataModel extends BaseModel {


    static final String TAG = DataModel.class.getSimpleName();


    public static final String NAME_COMIC = "comic";


    @StringDef({NAME_COMIC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DataPages {
    }

    public DataModel(MarvelApplication app, DatabaseWrapper database) {
        super(app, database);
    }


    public Data getData(@NonNull @DataPages String name) {

        return new Select().from(Data.class)
                .where(Data_Table.name.eq(name))
                .querySingle();
    }


    public synchronized void save(final Data data,@DataPages String name) {

        try {
            data.name = name;
            pruneInvalid(data);

            saveValid(data);

        } catch (ValidationFailedException ex) {
            return;
        }
    }

    private void saveValid(Data data) {
        Data stored = getData(data.name);
        if (stored == null) {
            data.save();
        } else {
            data.id = stored.id;
            data.update();
        }
    }


    public void delete(@DataPages String name) {
        new Delete()
                .from(Data.class)
                .where(Data_Table.name.eq(name))
                .query();

    }

    public void cleanTable() {
        new Delete().from(Data.class).query();

    }


}

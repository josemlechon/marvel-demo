package com.marvel.demo.domain.model;

import com.marvel.demo.app.MarvelApplication;
import com.marvel.demo.di.component.AppComponent;
import com.marvel.demo.domain.errors.ValidationFailedException;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

import java.util.Iterator;
import java.util.List;


/**
 * Created by jose m lechon on 23/07/16.
 * <p>
 * //Todo read https://github.com/codepath/android_guides/wiki/DBFlow-Guide
 *
 * @version 0.1.0
 * @since 1
 */
public class BaseModel {

    protected final DatabaseWrapper mSQLiteDatabase;

    protected final AppComponent mComponent;

    public BaseModel(MarvelApplication app, DatabaseWrapper database) {
        mComponent = app.getAppComponent();
        mSQLiteDatabase = database;
    }


    public static void pruneInvalid(List<? extends Validation> validations) {

        Iterator<? extends Validation> itr = validations.iterator();
        while (itr.hasNext()) {
            try {
                Validation next = itr.next();
                next.validate();
            } catch (ValidationFailedException ex) {
                itr.remove();
            }
        }
    }


    public static <T extends Validation> void pruneInvalid(T valid) throws ValidationFailedException {

        valid.validate();

    }

}

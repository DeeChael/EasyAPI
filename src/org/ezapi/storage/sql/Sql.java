package org.ezapi.storage.sql;

import org.ezapi.storage.Storable;

public interface Sql extends Storable, Closable, Reloadable {

    @Override
    default void setAll(Storable storable) {
        if (storable == null) return;
        if (storable instanceof Sql) {
            if (((Sql) storable).closed()) return;
        }
        for (String key : storable.keys()) {
            this.set(key, storable.get(key));
        }
    }

}

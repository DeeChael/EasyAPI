package org.ezapi.storage;

import java.util.List;

public interface Storage extends Storable {

    StorageContext get(String key, StorageContext defaultValue);

}

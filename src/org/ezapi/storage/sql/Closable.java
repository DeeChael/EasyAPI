package org.ezapi.storage.sql;

import java.sql.SQLException;

public interface Closable {

    void close() throws SQLException;

    boolean closed();

}

package nl.management.finance.app.data.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    private String id;

    @NonNull
    @ColumnInfo(name = "username")
    private String username;

    public User(@NonNull String id, @NonNull String username) {
        this.id = id;
        this.username = username;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}

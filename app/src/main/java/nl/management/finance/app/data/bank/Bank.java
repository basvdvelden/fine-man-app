package nl.management.finance.app.data.bank;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import nl.management.finance.app.BuildConfig;

@Entity(tableName = "bank")
public class Bank {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    public Bank(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public static Bank[] populationData() {
        return new Bank[]{new Bank(BuildConfig.RABO_BANK_ID, BuildConfig.RABO_BANK_NAME)};
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

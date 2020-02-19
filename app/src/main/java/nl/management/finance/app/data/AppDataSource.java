package nl.management.finance.app.data;

import java.io.IOException;

import retrofit2.Response;

public class AppDataSource {
    protected String getErrMsg(Response response) throws IOException {
        return response.errorBody() == null ? response.toString() : response.errorBody().string();
    }
}

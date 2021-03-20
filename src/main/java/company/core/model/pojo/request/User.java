package company.core.model.pojo.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {


    public UserUniform getUser() {
        return user;
    }

    public void setUser(UserUniform user) {
        this.user = user;
    }

    @JsonProperty("user")
    private UserUniform user;

}

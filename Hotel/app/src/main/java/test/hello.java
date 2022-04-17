package test;

import java.io.Serializable;

public class hello implements Serializable {
    private static final long serialVersionUID = 6944974904922463226L;
    private String user;
    private String pro;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }
}

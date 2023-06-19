package obligatorio.keys;

public class KeyUser {

    private String userKey;

    public KeyUser(String hashTagKey){
        this.userKey = hashTagKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    @Override
    public int hashCode(){
        int code = 3;
        for (int i = 0; i < this.userKey.length(); i++) {
            code = (code * 31) + this.userKey.charAt(i);
        }
        return Math.abs(code);
    }

    @Override
    public boolean equals (Object lengCodeNew){
        if (lengCodeNew instanceof KeyUser){
            KeyUser temp = (KeyUser) lengCodeNew;
            if (this.userKey.equals(temp.getUserKey())){
                return true;
            }
        }
        return false;
    }

}
